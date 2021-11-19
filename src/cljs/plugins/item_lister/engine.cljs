
(ns plugins.item-lister.engine
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.map        :refer [dissoc-in]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-environment.api :as environment]
              [x.app-sync.api        :as sync]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
;  Ha az item-lister komponenst alkalmazó extension nem gondoskodik időben
;  az [extension-name :lister-meta :order-by] értékének beállításáról,
;  akkor az item-lister az elemek letöltésekor a DEFAULT-ORDER-BY értékét alkalmazza.
(def DEFAULT-ORDER-BY :by-date-descending)

; @constant (keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS
     [:by-date-descending :by-date-ascending :by-name-descending :by-name-ascending])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-id
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @example
  ;  (item-lister/request-id "products" "product")
  ;  =>
  ;  :product-lister/synchronize!
  ;
  ; @return (keyword)
  [extension-name item-name]
  (keyword (str item-name "-lister") "synchronize!"))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing?
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [request-id (request-id extension-name item-name)]
       (r sync/listening-to-request? db request-id)))

(defn get-downloaded-items
  ; @param (string) extension-name
  ;
  ; @return (maps in vector)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (get-in db [extension-id :lister-data])))

(defn get-downloaded-item-count
  ; @param (string) extension-name
  ;
  ; @return (integer)
  [db [_ extension-name]]
  (let [downloaded-items (r get-downloaded-items db extension-name)]
       (count downloaded-items)))

(defn get-all-item-count
  ; @param (string) extension-name
  ;
  ; @return (integer)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       ; XXX#0791
       ; - Ha a tárolt érték nil, akkor a visszatérési érték 0
       ; - Ha a szerver hibásan nil értéket küld le, akkor a 0 visszatérési érték miatt
       ;   az all-items-downloaded? függvény visszatérési értéke true lesz ezért megáll
       ;   az újabb elemek letöltése.
       ; - Hibás szerver-működés esetén szükséges, hogy az infinite-loader komponens
       ;   ne próbálja újra és újra letölteni a további feltételezett elemeket.
       ; - Ha még nem történt meg az első kommunikáció a szerverrel, akkor a get-all-item-count
       ;   függvény visszatérési értéke nem tekinthető mérvadónak!
       ;   Az első kommunikáció megtörténtét, ezért szükséges külön vizsgálni!
       (let [all-item-count (get-in db [extension-id :lister-meta :document-count])]
            (if (integer? all-item-count)
                (return   all-item-count)
                (return   0)))))

(defn all-items-downloaded?
  ; @param (string) extension-name
  ;
  ; @return (boolean)
  [db [_ extension-name]]
  (let [all-item-count        (r get-all-item-count        db extension-name)
        downloaded-item-count (r get-downloaded-item-count db extension-name)]
       ; XXX#0791
       ; - = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
       ;   nagyobb a downloaded-item-count értéke, mint az all-item-count értéke,
       ;   akkor ne próbáljon további feltételezett elemeket letölteni.
       ; - Ha még nem történt meg az első kommunikáció a szerverrel, akkor az all-items-downloaded?
       ;   függvény visszatérési értéke nem tekinthető mérvadónak!
       ;   Az első kommunikáció megtörténtét, ezért szükséges külön vizsgálni!
       (println (str downloaded-item-count))
       (println (str all-item-count))
       (>= downloaded-item-count all-item-count)))

(defn get-search-term
  ; @param (string) extension-name
  ;
  ; @return (string)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)
        search-term  (get-in db [extension-id :lister-meta :search-term])]
       (str search-term)))

(defn get-order-by
  ; @param (string) extension-name
  ;
  ; @return (keyword)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (get-in db [extension-id :lister-meta :order-by] DEFAULT-ORDER-BY)))

(defn- download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @return (boolean)
  [db [_ extension-name item-name]]
  (let [request-id (request-id extension-name item-name)]
       (println (str extension-name))
       (println (str request-id))
       (println (str (not (r sync/request-sent?    db request-id))))
       (println (str (not (r all-items-downloaded? db extension-name))))
           ; XXX#0791
           ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
           ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
       (or (not (r sync/request-sent?    db request-id))
           (not (r all-items-downloaded? db extension-name)))))

(defn get-description
  ; @param (string) extension-name
  ;
  ; @return (string)
  [db [_ extension-name]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-name)
        all-item-count        (r get-all-item-count        db extension-name)]
       (components/content {:content      :npn-items-downloaded
                            :replacements [downloaded-item-count all-item-count]})))

(defn get-header-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/get-header-props db "products")
  ;
  ; @return (map)
  ;  {:viewport-small? (boolean)}
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       {:search-mode?    (get-in db [extension-id :lister-meta :search-mode?])
        :viewport-small? (r environment/viewport-small? db)}))

(defn get-view-props
  ; @param (string) extension-name
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)}
  [db [_ extension-name]]
  {:description (r get-description db extension-name)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-lister/toggle-search-mode! "products")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :lister-meta :search-mode?] not)))

(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn- toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r item-lister/toggle-select-mode! "products")
  ;
  ; @return (map)
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       (update-in db [extension-id :lister-meta :select-mode?] not)))

(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-lister/search-items! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [extension-id (keyword extension-name)]
           {:db       (-> db (dissoc-in [extension-id :lister-data])
                             (dissoc-in [extension-id :lister-meta :document-count]))
            :dispatch [:item-lister/request-items! extension-name item-name]})))

(a/reg-event-fx
  :item-lister/order-items!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-lister/order-items! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [extension-id (keyword extension-name)]
           {:db       (-> db (dissoc-in [extension-id :lister-data])
                             (dissoc-in [extension-id :lister-meta :document-count]))
            :dispatch [:item-lister/request-items! extension-name item-name]})))

(a/reg-event-fx
  :item-lister/receive-items!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-lister/receive-items! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name server-response]]
      (let [extension-id   (keyword extension-name)
            resolver-id    (keyword extension-name (str "get-" item-name "-items"))
            documents      (get-in server-response [resolver-id :documents])
            document-count (get-in server-response [resolver-id :document-count])]
           {:db       (-> db (update-in [extension-id :lister-data] vector/concat-items documents)
                             ; Szükséges frissíteni a keresési feltételeknek megfelelő
                             ; dokumentumok számát, mert változhat az értéke
                             (assoc-in  [extension-id :lister-meta :document-count] document-count))
            ; Az elemek letöltődése után, ha maradt még a szerveren letöltendő elem, akkor újratölti
            ; az infinite-loader komponenst, hogy megállapítsa, hogy az a viewport területén van-e még.
                         ;(r download-more-items? db "products")
            :dispatch-if [(r download-more-items? db extension-name item-name)
                         ;[:x.app-tools/reload-infinite-loader! :products]
                          [:x.app-tools/reload-infinite-loader! extension-id]]})))

(a/reg-event-fx
  :item-lister/request-items!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
  ; a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
  ; az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
  ; felirat még megjelenik a lista végén.
  ;
  ; @usage
  ;  [:item-lister/request-items! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
      (if (r download-more-items? db extension-name item-name)
          (let [request-id     (request-id extension-name item-name)
                resolver-id    (keyword extension-name (str "get-" item-name "-items"))
                resolver-props {:downloaded-item-count (r get-downloaded-item-count db extension-name)
                                :search-term           (r get-search-term           db extension-name)
                                :order-by              (r get-order-by              db extension-name)}]
              ;[:x.app-sync/send-query! :product-lister/synchronize!
               [:x.app-sync/send-query! request-id
                                        ;:on-stalled [:item-lister/receive-items! "products"]
                                        {:on-stalled [:item-lister/receive-items! extension-name item-name]
                                        ;:query      [`(:products/get-product-items {...})]
                                         :query      [`(~resolver-id ~resolver-props)]}]))))

(a/reg-event-fx
  :item-lister/load!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-lister/load! "products" "product"]
  (fn [{:keys [db]} [_ extension-name item-name]]
      (let [request-id   (request-id extension-name item-name)
            render-event (keyword extension-name (str "render-" item-name "-lister!"))
            extension-id (keyword extension-name)]
           {:db         (-> db (dissoc-in [extension-id :lister-data])
                               (dissoc-in [extension-id :lister-meta]))
                        ;[:x.app-ui/listen-to-process! :product-lister/synchronize!]
            :dispatch-n [[:x.app-ui/listen-to-process! request-id]
                        ;[:x.app-ui/set-header-title!  :products]
                         [:x.app-ui/set-header-title!  extension-id]
                        ;[:x.app-ui/set-window-title!  :products]
                         [:x.app-ui/set-window-title!  extension-id]
                        ;[:products/render-product-lister!]
                         [render-event]]})))

(a/reg-event-fx
  :item-lister/add-route!
  ; @param (string) extension-name
  ; @param (string) item-name
  ;
  ; @usage
  ;  [:item-lister/add-route! "products" "product"]
  (fn [_ [_ extension-name item-name]]
           ;route-id :products/route
      (let [route-id (keyword extension-name "route")]
           [:x.app-router/add-route! route-id
                                     ;:route-template "/products"
                                     {:route-template (str "/" extension-name)
                                     ;:route-event    [:item-lister/load! "products" "product"]
                                      :route-event    [:item-lister/load! extension-name item-name]
                                      :restricted?    true}])))
