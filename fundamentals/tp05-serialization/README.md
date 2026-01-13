## TP5: Serialization - Sending Objects

**Objective**: Send and receive Java objects instead of Strings

**Tasks**:
1. Create an `Order` class (id, product, quantity, price)
2. Implement a custom serializer or use a JSON library (Jackson/Gson)
3. Configure `value.serializer` to use JsonSerializer
4. Configure `value.deserializer` to use JsonDeserializer
5. Send Order objects and receive them

**Key Concepts**:
- Custom serializers/deserializers
- Serializer and Deserializer interfaces
- JSON serialization with external libraries
- Type mapping and ObjectMapper configuration

**Practice**:
- Try sending an Order and receiving it as a different class - what happens?
- Add a new field to Order class - is it backward compatible?
- Implement your own custom Serializer from scratch
