insert into tour_package (code, name) values
('GC-1', 'Grand Canyon'),
('JT-1', 'Joshua Tree'),
('LT-1', 'Lake Tahoe'),
('OG-1', 'Olive Garden');

insert into tour  (tour_package_code, tour_title, description, blurb, bullets, difficulty, duration, price, region, keywords) values
(
    'GC-1',
    'Grand Canyon West Rim Tour',
    'Grand Canyon West Rim is a fascinating tourist attraction site for camping',
    'The region know as Big Sur is like Yosemite''s younger cousin, with all the redwood scaling, rock climbing and, best of all, hiking that the larger park has to offer. Robison Jeffers once said, ''Big Sur is the greatest meeting of land and sea in the world,'' but the highlights are only accessible on foot.\nOur 3-day tour allows you to choose from multiple hikes led by experienced guides during the day, while comfortably situated in the evenings at the historic Big Sur River Inn. Take a tranquil walk to the coastal waterfall at Julia Pfeiffer Burns State Par or hike to the Married Redwoods. If you''re prepared for a more strenuous climb, try Ollason''s Peak in Toro Park. An optional 4th day includes admission to the Henry Miller Library and the Point Reyes Lighthouse.',
    'Accommodations at the historic Grand Canyon Inn, Privately guided hikes through any of the 5 surrounding national parks, Picnic lunches prepared by the Grand Canyon Inn kitchen, Complimentary country breakfast',
    'Medium',
    '5 days',
    '750',
    'Central Coast',
    'Hiking, National Parks, Grand Canyon'
);

insert into tour  (tour_package_code, tour_title, description, blurb, bullets, difficulty, duration, price, region, keywords) values
(
    'LT-1',
    'Ski Lake Tahoe',
    'Though technically shared with Nevada, we like to call Lake Tahoe our own. Unparalleled natural beauty, combined with world class trails make it the ultimate winter resort. Ski Lake Tahoe is ideal for serious skiers and snowboarders, but also offers fun for the whole family.',
    'Ski Lake Tahoe takes you on a 5 day, 4 night tour of the best slopes this side of Colorado. Included are 5 day lift tickets at Squaw Valley Resort, host of 1960 Winter Olympics, and home to over 170 runs on over 4000 acres of wilderness. Snow bunnies will rejoice but this tour is perfect for families and ''town bunnies'' alike. Accommodations are provided at the base of the slopes at The Village at Squaw Valley. Après-ski, enjoy relaxing in your private condo, or take advantage of the billiards room, the movie screening area, or one of the outdoor hot tubs. The Village has over 30 shops, restaurants and bars, offering everything from contemporary Irish cuisine to sushi. Enjoy wine tasting or luxuriate at the Trilogy Spa. Kids can try tubing, take on the climbing wall, or try the Skyjump, bouncing 25 feet in the air on giant trampolines.',
    '4 day lift tickets, 170 runs of 400 acres, Private condo at The Village at Squaw Valley, Over 30 shops and restaurants, Attractions for kids including wall climbing and trampolines',
    'Hard',
    '4 days',
    '1100',
    'Northern California',
    'Skiing, Snowboarding, Cabin, Hiking, Mountains'
),
(
    'OG-1',
    'Olive Garden Tour',
    'Take a quiet day to explore the lush hills and countryside around beautiful Ojai, California. You''ll visit Ojai Olive and tour the grounds, walk through the olive groves, and enjoy a once-in-a-lifetime lunch amongst the Ojai hills, featuring homemade tapas made from the olives on-site!',
    'Ojai Olive is a small, family-run olive orchard, which has been pressing their own special, fragrant olive oil for over 10 years. Immerse yourself in this ancient practice, which is both an art and an edible. The Ojai Olive tour offers begins outside with the history of the grove, an explanation of the olive varieties, their maturing process, the harvesting, a visit to the tree nursery, and more. They then take you inside the barn and explain the process of making the olive oil. Lastly, stop in the tasting room and find out how different curing and separation processes can result in such unique flavors: citrus, hints of pepper, grass. Take a break with a picnic lunch provided by Azu restaurant, makers of authentic Spanish style tapas with a California flair. Included are brioche with olive tapenade, featuring black and green olives from Ojai Olive and prepared by the chef especially for this tour.',
    'Tour of the olive orchards, Lessons on olive history and the maturing and harvesting process, Tour of the pressing rooms, Olive oil tasting from 5 unique varieties produced onsite, Picnic lunch with homemade tapas',
    'Easy',
    '1 day',
    '75',
    'Southern California',
    'Tasting, Olive Oil, California History, Picnic, Nature, Farming'
);

insert into tour (tour_package_code, tour_title, description, blurb, bullets, difficulty, duration, price, region, keywords) values
(
    'JT-1',
    'Joshua Tree: Best of the West Tour',
    'See Joshua Trees''backcountry on a guided tour from the safety and security of an open-air, all-terrain vehicle, and see some of the historic spots of California''s once wild, Wild West frontier.',
    'Joshua Tree: Best of the West Tour is a day-long adventure through this grandiose national park and a snippet of wild west history from the back of an open-air all-terrain vehicle. This is a great opportunity for photo hunters to shots of the coyotes, eagles and Big Horn sheep native to the area. Multiple stops includes a visit to the Rock Gardens, a 200 million year old legacy of the San Andreas Fault. The tour stops last at Pioneertown, originally built as a motion picture set in the 1940s, but has since becomes part of California''s history in its own right, as a living signpost of our fascination with the legends of the Wild West. Enjoy mesquite smoked barbeque and marvel at the ''bullet holes'' and functional hitching posts. The tour ends with a stop at Sunset Point, where on a clear day, you can see all the way to Mexico.',
    'Park admission, souvenir map, mesquite barbeque dinner, luxury H-1 best in class Hummer, expert guide, 2 hours off the Humvee for optional walks and photo stops, and an hour-long tour of Pioneertown.',
    'Easy',
    '1 day',
    '150',
    'Southern California',
    'Hiking, Desert, Sea, California History, Theme park'
);

insert into tour_rating (tour_id, customer_id, rating_score, comment) values
(1, 4, 5, 'I loved it'),
(2, 100, 5, 'I really thought it could have been better');

insert into user (user_id, username, password, first_name, last_name, email) values
(1, 'admin', '$2a$10$O7P841p7ui75riNDdGuMMurxVosky3sTE19j9eK2E6Wf.m59teNHG', 'admin', 'admin', 'admin@travel.com'),
(2, 'csr_tom', '$2y$12$mMgV9eakGVSZ3IxeSW7CR.5NrDmrvnqtsBzfz7uwZZU2PRJFa4L6a', 'Tom', 'Miles', 'tom.cook@travel.com'),
(3, 'jacky', '$2a$10$DvSji4zakHEB4oixZqrWuk0mBU2WuXcFpMvFqnSj5/9vgUEM8LCi', 'Jacky', 'Huges', 'jacky.huges@travel.com');

insert into auth_user_group (auth_user_group_id, auth_group, description) values
(1, 'CSR_ADMIN', 'CSR Administrator group access'),
(2, 'CSR_USER', 'CSR User group access'),
(3, 'VIEWER', 'Viewer has only viewing access');

insert into user_auth_user_group (username, auth_user_group_id) values
('admin', 1),
('csr_tom', 2),
('jacky', 3);
