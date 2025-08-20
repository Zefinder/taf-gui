/*
 * Copyright or © or Copr.
 * 
 * This software is a computer program whose purpose is to generate random test
 * case from a template file describing the data model.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-B license as
 * circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * 
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * 
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systems and/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package com.taf.util;

import java.util.ArrayList;
import java.util.List;

import com.taf.exception.RangeException;
import com.taf.exception.RangeIntersectionException;

/**
 * A RangeTree represents a tree of non overlapping integer ranges. It allows
 * fast integer research and can be used to build a RangeMap if needed. It is
 * possible to renumber the ranges, but in the depth-first format.
 *
 * @author Adrien Jakubiak
 */
public class RangeTree {

	private static final String RANGE_INTERSECTION_ERROR_MESSAGE = "Ranges intersect, abort...";

	/** The root node. */
	private Node rootNode;

	/** The tree max node id. */
	private int maxNodeId;

	/**
	 * Instantiates a new range tree.
	 */
	public RangeTree() {
		rootNode = new Node(0);
	}

	/**
	 * Adds a range to the tree. If the range intersects with an already existing
	 * range, a {@link RangeException} is thrown.
	 *
	 * @param start the range start
	 * @param end   the range end
	 * @throws RangeException if the new range intersects with a range present in
	 *                        the range tree
	 */
	public void addRange(int start, int end) throws RangeException {
		Range range = new Range(start, end);
		rootNode.addRange(range, maxNodeId++);
	}

	/**
	 * Returns the parent id of the range at the specified id.
	 *
	 * @param rangeId the range id
	 * @return the parent id
	 */
	public int getParentId(int rangeId) {
		return rootNode.getParentId(rangeId);
	}

	/**
	 * Returns the id of the range containing the input number n, or -1 if no range
	 * contains it.
	 *
	 * @param n the number to check
	 * @return the id of the range containing the input number n, or -1.
	 */
	public int getRangeId(int n) {
		return rootNode.getNodeId(n);
	}

	/**
	 * Numbers the tree in a depth-first style (starting with 1). Id 0 is reserved
	 * for the root.
	 */
	public void numberTree() {
		rootNode.numberNode(0);
	}

	@Override
	public String toString() {
		return rootNode.toString();
	}

	/**
	 * The class that represents a node in the range tree.
	 * 
	 * @see RangeTree
	 *
	 * @author Adrien Jakubiak
	 */
	private static class Node {

		/** The node range. */
		private Range range;

		/** The node id. */
		private int nodeId;

		/** The node children. */
		private List<RangeTree.Node> children;

		/**
		 * Instantiates a new node with a specified id.
		 *
		 * @param nodeId the node id
		 */
		public Node(int nodeId) {
			this(null, nodeId);
		}

		/**
		 * Instantiates a new node with a specified range and id.
		 *
		 * @param range  the node range
		 * @param nodeId the node id
		 */
		public Node(Range range, int nodeId) {
			this.range = range;
			this.nodeId = nodeId;
			children = new ArrayList<RangeTree.Node>();
		}

		/**
		 * Adds a range to the node. It throws a {@link RangeIntersectionException} if
		 * it intersects with one of the node children.
		 *
		 * @param range     the range to add
		 * @param maxNodeId the max node id
		 * @throws RangeIntersectionException if the new range intersects with one of
		 *                                    the children
		 */
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
						throw new RangeIntersectionException(RangeTree.class, RANGE_INTERSECTION_ERROR_MESSAGE);
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

		/**
		 * Returns the node id at the specified position.
		 *
		 * @param position the position
		 * @return the node id
		 */
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

					// If nothing was found, it means that it is in the last child
					return children.get(children.size() - 1).getParentId(nodeId);
				}
			}

			return -1;
		}

		/**
		 * Number the node and its children in a depth-first style.
		 *
		 * @param nodeNumber the current node number
		 * @return the new node number after numbering all children
		 */
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

	/**
	 * Represents a integer range that is used in the range tree.
	 * 
	 * @see RangeTree
	 *
	 * @author Adrien Jakubiak
	 */
	private static class Range {

		private static final String RANGE_BOUNDS_ERROR_MESSAGE = "Error in range creation: start > end!";

		/** The range start. */
		private int start;

		/** The range end. */
		private int end;

		/**
		 * Represents the range [start; end]. Start must be lower or equal to end.
		 *
		 * @param start the start
		 * @param end   the end
		 * @throws RangeException if start > end
		 */
		public Range(int start, int end) throws RangeException {
			if (start > end) {
				throw new RangeException(Range.class, RANGE_BOUNDS_ERROR_MESSAGE);
			}
			this.start = start;
			this.end = end;
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

		/**
		 * Checks if the value is in this range.
		 *
		 * @param value the value
		 * @return true if is in range
		 */
		public boolean isInRange(int value) {
			return value >= start && value <= end;
		}
	}

}
