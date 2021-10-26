package com.algaworks.algafoodapi.core.validation;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import org.springframework.beans.BeanUtils;

public class ValorZeroIncluiDescricaoValidator implements
    ConstraintValidator<ValorZeroIncluiDescricao, Object> {

  private String valorField;
  private String descricaoField;
  private String descricaoObrigatoria;

  @Override
  public void initialize(ValorZeroIncluiDescricao constraint) {
    this.descricaoField = constraint.descricaoField();
    this.valorField = constraint.valorField();
    this.descricaoObrigatoria = constraint.descricaoObrigatoria();
  }

  @Override
  public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
    boolean valido = true;

    try {
      BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(),
          valorField).getReadMethod().invoke(objetoValidacao);

      String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(),
              descricaoField)
          .getReadMethod().invoke(objetoValidacao);

      if (Objects.nonNull(valor) && BigDecimal.ZERO.compareTo(valor) == 0 && Objects.nonNull(descricao)) {
        valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
      }
      return valido;
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }
}
