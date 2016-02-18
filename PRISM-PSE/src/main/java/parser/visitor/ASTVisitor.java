//==============================================================================
//	
//	Copyright (c) 2002-
//	Authors:
//	* Dave Parker <d.a.parker@cs.bham.ac.uk> (University of Birmingham/Oxford)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package parser.visitor;

import parser.ast.Command;
import parser.ast.ConstantList;
import parser.ast.Declaration;
import parser.ast.DeclarationArray;
import parser.ast.DeclarationBool;
import parser.ast.DeclarationClock;
import parser.ast.DeclarationInt;
import parser.ast.DeclarationIntUnbounded;
import parser.ast.ExpressionBinaryOp;
import parser.ast.ExpressionConstant;
import parser.ast.ExpressionExists;
import parser.ast.ExpressionFilter;
import parser.ast.ExpressionForAll;
import parser.ast.ExpressionFormula;
import parser.ast.ExpressionFunc;
import parser.ast.ExpressionITE;
import parser.ast.ExpressionIdent;
import parser.ast.ExpressionLabel;
import parser.ast.ExpressionLiteral;
import parser.ast.ExpressionProb;
import parser.ast.ExpressionProp;
import parser.ast.ExpressionReward;
import parser.ast.ExpressionSS;
import parser.ast.ExpressionStrategy;
import parser.ast.ExpressionTemporal;
import parser.ast.ExpressionUnaryOp;
import parser.ast.ExpressionVar;
import parser.ast.Filter;
import parser.ast.ForLoop;
import parser.ast.FormulaList;
import parser.ast.LabelList;
import parser.ast.Module;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import parser.ast.Property;
import parser.ast.RenamedModule;
import parser.ast.RewardStruct;
import parser.ast.RewardStructItem;
import parser.ast.SystemBrackets;
import parser.ast.SystemFullParallel;
import parser.ast.SystemHide;
import parser.ast.SystemInterleaved;
import parser.ast.SystemModule;
import parser.ast.SystemParallel;
import parser.ast.SystemReference;
import parser.ast.SystemRename;
import parser.ast.Update;
import parser.ast.Updates;
import prism.PrismLangException;

public interface ASTVisitor
{
	// ASTElement classes (model/properties file)
	public Object visit(ModulesFile e) throws PrismLangException;
	public Object visit(PropertiesFile e) throws PrismLangException;
	public Object visit(Property e) throws PrismLangException;
	public Object visit(FormulaList e) throws PrismLangException;
	public Object visit(LabelList e) throws PrismLangException;
	public Object visit(ConstantList e) throws PrismLangException;
	public Object visit(Declaration e) throws PrismLangException;
	public Object visit(DeclarationInt e) throws PrismLangException;
	public Object visit(DeclarationBool e) throws PrismLangException;
	public Object visit(DeclarationArray e) throws PrismLangException;
	public Object visit(DeclarationClock e) throws PrismLangException;
	public Object visit(DeclarationIntUnbounded e) throws PrismLangException;
	public Object visit(Module e) throws PrismLangException;
	public Object visit(Command e) throws PrismLangException;
	public Object visit(Updates e) throws PrismLangException;
	public Object visit(Update e) throws PrismLangException;
	public Object visit(RenamedModule e) throws PrismLangException;
	public Object visit(RewardStruct e) throws PrismLangException;
	public Object visit(RewardStructItem e) throws PrismLangException;
	// ASTElement/SystemDefn classes
	public Object visit(SystemInterleaved e) throws PrismLangException;
	public Object visit(SystemFullParallel e) throws PrismLangException;
	public Object visit(SystemParallel e) throws PrismLangException;
	public Object visit(SystemHide e) throws PrismLangException;
	public Object visit(SystemRename e) throws PrismLangException;
	public Object visit(SystemModule e) throws PrismLangException;
	public Object visit(SystemBrackets e) throws PrismLangException;
	public Object visit(SystemReference e) throws PrismLangException;
	// ASTElement/Expression classes
	public Object visit(ExpressionTemporal e) throws PrismLangException;
	public Object visit(ExpressionITE e) throws PrismLangException;
	public Object visit(ExpressionBinaryOp e) throws PrismLangException;
	public Object visit(ExpressionUnaryOp e) throws PrismLangException;
	public Object visit(ExpressionFunc e) throws PrismLangException;
	public Object visit(ExpressionIdent e) throws PrismLangException;
	public Object visit(ExpressionLiteral e) throws PrismLangException;
	public Object visit(ExpressionConstant e) throws PrismLangException;
	public Object visit(ExpressionFormula e) throws PrismLangException;
	public Object visit(ExpressionVar e) throws PrismLangException;
	public Object visit(ExpressionProb e) throws PrismLangException;
	public Object visit(ExpressionReward e) throws PrismLangException;
	public Object visit(ExpressionSS e) throws PrismLangException;
	public Object visit(ExpressionExists e) throws PrismLangException;
	public Object visit(ExpressionForAll e) throws PrismLangException;
	public Object visit(ExpressionStrategy e) throws PrismLangException;
	public Object visit(ExpressionLabel e) throws PrismLangException;
	public Object visit(ExpressionProp e) throws PrismLangException;
	public Object visit(ExpressionFilter e) throws PrismLangException;
	// ASTElement classes (misc.)
	public Object visit(Filter e) throws PrismLangException;
	public Object visit(ForLoop e) throws PrismLangException;
}

