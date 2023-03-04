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

import java.util.ArrayList;
import java.util.List;

import com.ubiqube.etsi.mano.service.cond.AstException;
import com.ubiqube.etsi.mano.service.cond.Node;
import com.ubiqube.etsi.mano.service.cond.SimpleNodeReturn;
import com.ubiqube.etsi.mano.service.cond.ast.GenericCondition;
import com.ubiqube.etsi.mano.service.cond.ast.LabelExpression;
import com.ubiqube.etsi.mano.service.cond.ast.PatternValueExpr;

import jakarta.annotation.Nonnull;

public class NodeIndicatorVisitor extends SimpleNodeReturn<Node> {
	@Nonnull
	private final List<String> results = new ArrayList<>();

	@Override
	public Node visit(final LabelExpression expr, final Node arg) {
		results.add(expr.name());
		return super.visit(expr, arg);
	}

	@Override
	public Node visit(final PatternValueExpr expr, final Node arg) {
		final Node left = expr.getLeft();
		if (!(left instanceof final LabelExpression le)) {
			throw new AstException(" " + left.getClass().getSimpleName());
		}
		results.add(le.name());
		return expr;
	}

	@Override
	public Node visit(final GenericCondition expr, final Node arg) {
		expr.getLeft().accept(this, arg);
		expr.getRight().accept(this, arg);
		return expr;
	}

	public List<String> getResults() {
		return results;
	}

}
