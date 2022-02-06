
# Example A  
# :(

```
(defn my-f
  []
  (let [state @(subscribe [:get-my-state])
        ref    (random-uuid)]
       [:div (str random-uuid)]))
 ```

- Az let függvényben dereferált atom a let függvény további értékeit is újraszámításra kényszeríti!
- A fenti példában, ha megváltozik a [:get-my-state] feliratkozás visszatérési értéke, akkor a ref
  értéke is megváltozik!



# Example B
# :(

 ```
(defn your-f
  [state]
  (let [ref (random-uuid)]
       [:div (str ref)]))

(defn my-f
  []
  (let [state @(subscribe [:get-my-state])]
       [your-f state]))           
```

- A komponens megváltozó paramétere a let függvény értékeit újraszámításra kényszeríti!
- A fenti példában, ha megváltozik a [:get-my-state] feliratkozás visszatérési értéke, akkor
  a your-f függvény ref értéke is megváltozik!



# Example C
# :)

 ```
(defn my-f
  []
  (let [state (subscribe [:get-my-state])
        ref   (random-uuid)]
       (fn [] [:div (str ref @state)])))           
```

- A komponensben különálló render függvényben dereferált atom a let függvény értékeit NEM kényszeríti
  újraszámításra!
- A fenti példában, ha megváltozik a [:get-my-state] feliratkozás visszatérési értéke, akkor a ref
  értéke NEM változik meg!



# Example D
# :(

```
(defn my-compute
  [n]
  (letfn [(f [%1 %2] (str %1 (random-uuid)))])
         (reduce f "" (range n)))

(defn get-my-state
  [db _]
  {:ref (my-compute 1000)})

(reg-sub :get-my-state get-my-state)  

(defn my-f
  []
  (let [state @(subscribe [:get-my-state])]
       [:div (:ref state)]))
```

- A subscription függvényben végzett számítások a Re-Frame adatbázis minden megváltozásakor lefutnak!
- A fenti példában a get-my-state feliratkozásban kiszámított my-compute függvény a Re-Frame adatbázis
  MINDEN megváltozásakor újraszámítódik!



# Example E
# :)

```
(defn my-compute
  [n]
  (letfn [(f [%1 %2] (str %1 (random-uuid)))])
         (reduce f "" (range n)))

(defn get-my-state
  [db _]
  {:ref (get db :my-state)})

(reg-sub :get-my-state get-my-state)          

(defn set-my-state!
  [db _]
  (assoc db :my-state (my-compute 1000)))

(reg-event-db :set-my-state! set-my-state!)  

(defn my-f
  []
  (let [state @(subscribe [:get-my-state])]
       [:div {:on-click #(dispatch [:set-my-state!])}
             (:ref state)]))
```

- A fenti példában a set-my-state! függvényben kiszámított my-compute függvény CSAK a [:set-my-state!]
  esemény megtörténésekor számítódik ki!
