package com.Reap.ReapProject.event;


import com.Reap.ReapProject.entity.Item;
import com.Reap.ReapProject.repository.ItemRepository;
import com.Reap.ReapProject.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ItemBootstrap {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;


    @EventListener(ContextRefreshedEvent.class)
    void setUp() {
        if (!itemRepository.findAll().iterator().hasNext()) {
            System.out.println("Bootstrapping items");

            System.out.println("Bootstrapping item one");
            Item item1 = new Item();
            item1.setName("To The New T-Shirt");
            item1.setPoints(100);
            item1.setQuantity(50);
            item1.setImage("/images/tshirt.jpg");
            itemService.saveItem(item1);
            System.out.println(item1.toString());

            System.out.println("Bootstrapping item two");
            Item item2 = new Item();
            item2.setName("To The New Cap");
            item2.setPoints(70);
            item2.setQuantity(100);
            item2.setImage("/images/cap.jpg");
            itemService.saveItem(item2);
            System.out.println(item2.toString());

            System.out.println("Bootstrapping item three");
            Item item3 = new Item();
            item3.setName("To The New Backpack");
            item3.setPoints(150);
            item3.setQuantity(70);
            item3.setImage("/images/backpack.jpg");
            itemService.saveItem(item3);
            System.out.println(item3.toString());

            System.out.println("Bootstrapping item four");
            Item item4 = new Item();
            item4.setName("To The New Bottle");
            item4.setPoints(80);
            item4.setQuantity(100);
            item4.setImage("/images/bottle.png");
            itemService.saveItem(item4);
            System.out.println(item4.toString());

            System.out.println("Bootstrapping item five");
            Item item5 = new Item();
            item5.setName("Staples Easy Button");
            item5.setPoints(200);
            item5.setQuantity(30);
            item5.setImage("/images/easy-button.jpg");
            itemService.saveItem(item5);
            System.out.println(item5.toString());

            System.out.println("Bootstrapping item six");
            Item item6 = new Item();
            item6.setName("To The New Keychain");
            item6.setPoints(30);
            item6.setQuantity(150);
            item6.setImage("/images/keychain.jpg");
            itemService.saveItem(item6);
            System.out.println(item6.toString());

            System.out.println("Bootstrapping item seven");
            Item item7 = new Item();
            item7.setName("Spiral Notebook + Pen Set");
            item7.setPoints(40);
            item7.setQuantity(100);
            item7.setImage("/images/notebook.jpg");
            itemService.saveItem(item7);
            System.out.println(item7.toString());

            System.out.println("Bootstrapping item eight");
            Item item8 = new Item();
            item8.setName("Passport/Travel Wallet");
            item8.setPoints(130);
            item8.setQuantity(50);
            item8.setImage("/images/passport-wallet.jpg");
            itemService.saveItem(item8);
            System.out.println(item8.toString());

            System.out.println("Bootstrapping item nine");
            Item item9 = new Item();
            item9.setName("Stationery Organizer");
            item9.setPoints(50);
            item9.setQuantity(100);
            item9.setImage("/images/stationery-organizer.jpg");
            itemService.saveItem(item9);
            System.out.println(item9.toString());

            System.out.println("Bootstrapping item ten");
            Item item10 = new Item();
            item10.setName("Belkin 10000 mAh Power Bank");
            item10.setPoints(200);
            item10.setQuantity(60);
            item10.setImage("/images/power-bank.jpeg");
            itemService.saveItem(item10);
            System.out.println(item10.toString());

            System.out.println("Bootstrapping item eleven");
            Item item11 = new Item();
            item11.setName("Sennheiser HD4.20S Over-ear Headphones");
            item11.setPoints(400);
            item11.setQuantity(20);
            item11.setImage("/images/headphones.jpg");
            itemService.saveItem(item11);
            System.out.println(item11.toString());

            System.out.println("Bootstrapping item twelve");
            Item item12 = new Item();
            item12.setName("Paper-weight");
            item12.setPoints(50);
            item12.setQuantity(100);
            item12.setImage("/images/paper-weight.jpg");
            itemService.saveItem(item12);
            System.out.println(item12.toString());

            System.out.println("Bootstrapping item thirteen");
            Item item13 = new Item();
            item13.setName("Desk Vases (Set of 4)");
            item13.setPoints(100);
            item13.setQuantity(60);
            item13.setImage("/images/desk-vase.jpg");
            itemService.saveItem(item13);
            System.out.println(item13.toString());

            System.out.println("Bootstrapping item fourteen");
            Item item14 = new Item();
            item14.setName("To The New Large Coffee Mug");
            item14.setPoints(40);
            item14.setQuantity(300);
            item14.setImage("/images/mug.jpg");
            itemService.saveItem(item14);
            System.out.println(item14.toString());

            System.out.println("Bootstrapping item fifteen");
            Item item15 = new Item();
            item15.setName("To The New Laptop Sticker");
            item15.setPoints(20);
            item15.setQuantity(500);
            item15.setImage("/images/sticker.jpg");
            itemService.saveItem(item15);
            System.out.println(item15.toString());
        }

    }
}
