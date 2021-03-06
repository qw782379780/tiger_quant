package com.tigerbrokers.quant.gateway;

import com.tigerbrokers.quant.event.Event;
import com.tigerbrokers.quant.event.EventEngine;
import com.tigerbrokers.quant.event.EventType;
import com.tigerbrokers.quant.model.data.Account;
import com.tigerbrokers.quant.model.data.Asset;
import com.tigerbrokers.quant.model.data.Bar;
import com.tigerbrokers.quant.model.data.Contract;
import com.tigerbrokers.quant.model.data.Order;
import com.tigerbrokers.quant.model.data.Position;
import com.tigerbrokers.quant.model.data.Tick;
import com.tigerbrokers.quant.model.data.Trade;
import com.tigerbrokers.quant.model.enums.BarType;
import com.tigerbrokers.quant.model.request.ModifyRequest;
import com.tigerbrokers.quant.model.request.OrderRequest;
import com.tigerbrokers.quant.model.request.SubscribeRequest;
import com.tigerbrokers.stock.openapi.client.https.domain.quote.item.KlinePoint;
import com.tigerbrokers.stock.openapi.client.struct.enums.KType;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Description:
 *
 * @author kevin
 * @date 2019/08/15
 */
@Data
public abstract class Gateway {

  private EventEngine eventEngine;
  private String gatewayName;

  public Gateway(EventEngine eventEngine, String gatewayName) {
    this.eventEngine = eventEngine;
    this.gatewayName = gatewayName;
  }

  public void onEvent(EventType eventType, Object data) {
    eventEngine.put(new Event(eventType, data));
  }

  public void onTick(Tick tick) {
    onEvent(EventType.EVENT_TICK, tick);
  }

  public void onTrade(Trade trade) {
    onEvent(EventType.EVENT_TRADE, trade);
  }

  public void onOrder(Order order) {
    onEvent(EventType.EVENT_ORDER, order);
  }

  public void onPosition(Position position) {
    onEvent(EventType.EVENT_POSITION, position);
  }

  public void onAccount(Account account) {
    onEvent(EventType.EVENT_ACCOUNT, account);
  }

  public void onContract(Contract contract) {
    onEvent(EventType.EVENT_CONTRACT, contract);
  }

  public void onAsset(Asset asset) {
    onEvent(EventType.EVENT_ASSET, asset);
  }

  public void log(String format, Object... args) {
    if (format == null || format.isEmpty()) {
      return;
    }
    FormattingTuple formatting = MessageFormatter.arrayFormat(format, args);
    onEvent(EventType.EVENT_LOG, formatting.getMessage());
  }

  public void stop() {

  }

  public abstract void connect();

  public abstract void subscribe(SubscribeRequest request);

  public abstract String sendOrder(OrderRequest request);

  public abstract void cancelOrder(ModifyRequest request);

  public abstract void modifyOrder(ModifyRequest request);

  public abstract Map<String, List<Bar>> getBars(List<String> symbols, BarType barType, int limit);
}
