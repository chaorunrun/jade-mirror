package jade.content.onto;

// #APIDOC_EXCLUDE_FILE

public class NotAnAggregate extends OntologyException {

  public NotAnAggregate() {
    super("");
  }

  @Override
  public Throwable fillInStackTrace() {
    return this;
  }
}
