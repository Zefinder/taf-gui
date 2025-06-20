package com.taf.util;

import java.util.ArrayList;
import java.util.List;

import com.taf.exception.RangeIntersectionException;

public class RangeTree {

	private static final String RANGE_INTERSECTION_ERROR_MESSAGE = "Ranges intersect!";

	private Node rootNode;
	private int maxNodeId;

	public RangeTree() {
		rootNode = new Node(0);
	}

	public void addRange(int start, int end) throws RangeIntersectionException {
		Range range = new Range(start, end);
		rootNode.addRange(range, maxNodeId++);
	}

	public int getParentId(int nodeId) {
		return rootNode.getParentId(nodeId);
	}

	public int getNodeId(int position) {
		return rootNode.getNodeId(position);
	}

	public void numberTree() {
		rootNode.numberNode(0);
	}

	@Override
	public String toString() {
		return rootNode.toString();
	}

	private static class Node {
		private Range range;
		private int nodeId;
		private List<RangeTree.Node> children;

		public Node(Range range, int nodeId) {
			this.range = range;
			this.nodeId = nodeId;
			children = new ArrayList<RangeTree.Node>();
		}

		public Node(int nodeId) {
			this(null, nodeId);
		}

		public void addRange(Range range, int maxNodeId) throws RangeIntersectionException {
			List<Node> enclosedNodeIndexes = new ArrayList<Node>();

			// Check first if range is enclosed in a child
			for (Node child : children) {
				if (child.range.encloses(range)) {
					// Add to child
					child.addRange(range, maxNodeId);
					return;
				} else {
					// Check if the new range encloses the child
					if (range.encloses(child.range)) {
						enclosedNodeIndexes.add(child);
					}

					// Check if intersects
					if (child.range.intersects(range)) {
						// ERROR should not be possible
						throw new RangeIntersectionException(RANGE_INTERSECTION_ERROR_MESSAGE);
					}
				}
			}

			// If not in a child, then add to this node!
			Node newChild = new Node(range, maxNodeId + 1);
			for (Node enclosedNode : enclosedNodeIndexes) {
				children.remove(enclosedNode);
				newChild.children.add(enclosedNode);
			}
			children.add(newChild);
		}

		public int getNodeId(int position) {
			// If this node is in range (or range null because root), check for children
			if (range == null || range.isInRange(position)) {
				for (Node child : children) {
					int childNodeId = child.getNodeId(position);
					if (childNodeId != -1) {
						// Child has the value
						return childNodeId;
					}
				}

				// Children do not have the value, return this id
				return nodeId;
			}

			// Nothing found, return -1
			return -1;
		}

		/**
		 * Returns the node's parent id using the node id. Returns -1 if the node id
		 * does not exist. This method assumes that the tree has been correctly numbered
		 * using {@link #numberNode(int)}! Unexpected behavior if the tree has not been
		 * numbered before.
		 * 
		 * @param nodeId the node id.
		 * @return the node's parent id, or -1 if the node does not exist.
		 */
		public int getParentId(int nodeId) {
			if (!children.isEmpty()) {
				if (children.size() == 1) {
					// If only one child, check if parent inside iff the id is not the same
					Node child = children.get(0);
					
					if (nodeId == child.nodeId) {
						return this.nodeId;
					} else {
						return child.getParentId(nodeId);
					}
					
				} else {					
					for (int i = 1; i < children.size(); i++) {
						Node lowChild = children.get(i - 1);
						Node highChild = children.get(i);
						
						// Check if one id is equal to either child
						if (nodeId == lowChild.nodeId || nodeId == highChild.nodeId) {
							return this.nodeId;
						} else if (nodeId < highChild.nodeId) {
							// Else, if the high child has a greater id then it is a child of the low child
							return lowChild.getParentId(nodeId);
						}
						
						// Else continue
					}
				}
			}

			return -1;
		}

		public int numberNode(int nodeNumber) {
			nodeId = nodeNumber;
			for (Node child : children) {
				nodeNumber = child.numberNode(nodeNumber + 1);
			}
			return nodeNumber;
		}

		@Override
		public String toString() {
			int start = 0;
			int end = 0;
			if (range != null) {
				start = range.start;
				end = range.end;
			}

			return "%d: [%d, %d] -> %s".formatted(nodeId, start, end, children.toString());
		}
	}

	private static class Range {
		private int start;
		private int end;

		/**
		 * Represents the range [start; end]
		 * 
		 * @param start
		 * @param end
		 */
		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public boolean isInRange(int value) {
			return value >= start && value <= end;
		}

		/**
		 * Checks whether this range encloses the other range. The enclosure check is
		 * strict, meaning that [a; b] encloses [c; d] iff a < c and b > d.
		 * 
		 * @param other the other range
		 * @return true if this range encloses the other range
		 */
		public boolean encloses(Range other) {
			return other.start > start && other.end < end;
		}

		/**
		 * Checks whether this range and the other range intersect.
		 * 
		 * @param other the other range
		 * @return true if this range intersects the other range
		 */
		public boolean intersects(Range other) {
			return (other.start >= start && other.start <= end) || (other.end >= start && other.end <= end);
		}
	}

}
