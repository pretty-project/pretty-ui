
(ns plugins.item-lister.engine
    (:require [mid-fruits.map        :refer [dissoc-in]]
              [mid-fruits.vector     :as vector]
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing?
  ; @param (string) extension-name
  ;
  ; @return (boolean)
  [db [_ extension-name]]
  (r sync/listening-to-request? db :item-lister/synchronize!))

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
       (get-in db [extension-id :lister-meta :document-count])))

(defn all-items-downloaded?
  ; @param (string) extension-name
  ;
  ; @return (boolean)
  [db [_ extension-name]]
  (let [all-item-count        (r get-all-item-count        db extension-name)
        downloaded-item-count (r get-downloaded-item-count db extension-name)]
       (= all-item-count downloaded-item-count)))

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

(defn get-header-view-props
  ; @param (string) extension-name
  ;
  ; @usage
  ;  (r pattern/get-item-list-header-view-props db "products")
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
  [db [_ extension-name]]
  (let [extension-id (keyword extension-name)]
       {:downloaded-item-count (r get-downloaded-item-count db extension-name)
        :all-item-count        (r get-all-item-count        db extension-name)}))



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
                         ;(not (r all-items-downloaded? db "products")
            :dispatch-if [(not (r all-items-downloaded? db extension-name))
                         ;[:x.app-components/reload-infinite-loader! :products]
                          [:x.app-components/reload-infinite-loader! extension-id]]})))

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
      (if-not (r all-items-downloaded? db extension-name)
              (let [resolver-id    (keyword extension-name (str "get-" item-name "-items"))
                    resolver-props {:downloaded-item-count (r get-downloaded-item-count db extension-name)
                                    :search-term           (r get-search-term           db extension-name)
                                    :order-by              (r get-order-by              db extension-name)}]
                   [:x.app-sync/send-query! :item-lister/synchronize!
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
      (let [render-event (keyword extension-name (str "render-" item-name "-lister!"))
            extension-id (keyword extension-name)]
           {:db         (dissoc-in db [extension-id :lister-data])
            :dispatch-n [[:x.app-ui/listen-to-process! :item-lister/synchronize!]
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
