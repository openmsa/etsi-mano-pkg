/**
 *     Copyright (C) 2019-2023 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ubiqube.etsi.mano.service.cond.AstException;
import com.ubiqube.etsi.mano.service.cond.Node;
import com.ubiqube.etsi.mano.service.cond.ast.GenericCondition;
import com.ubiqube.etsi.mano.service.cond.ast.LabelExpression;
import com.ubiqube.etsi.mano.service.cond.ast.PatternValueExpr;

/**
 *
 * @author Olivier Vignaud
 *
 */
class NodeIndicatorVisitorTest {

	@Test
	void testLabelExpr() {
		final NodeIndicatorVisitor srv = new NodeIndicatorVisitor();
		final LabelExpression expr = new LabelExpression("name");
		srv.visit(expr, null);
		srv.getResults();
		assertTrue(true);
	}

	@Test
	void testPatternValueExpr() {
		final NodeIndicatorVisitor srv = new NodeIndicatorVisitor();
		final PatternValueExpr expr = new PatternValueExpr("test");
		expr.setLeft(expr);
		assertThrows(AstException.class, () -> srv.visit(expr, null));
	}

	@Test
	void testPatternValueExpr2() {
		final NodeIndicatorVisitor srv = new NodeIndicatorVisitor();
		final PatternValueExpr expr = new PatternValueExpr("test");
		final Node node = new LabelExpression("left");
		expr.setLeft(node);
		srv.visit(expr, null);
		srv.getResults();
		assertTrue(true);
	}

	@Test
	void testGenericCondition() {
		final NodeIndicatorVisitor srv = new NodeIndicatorVisitor();
		final Node node = new LabelExpression("left");
		final GenericCondition expr = new GenericCondition(node, null, node);
		srv.visit(expr, node);
		srv.getResults();
		assertTrue(true);
	}
}
