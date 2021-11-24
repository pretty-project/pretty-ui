
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.engine
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.map        :refer [dissoc-in]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-environment.api :as environment]
              [x.app-sync.api        :as sync]
              [mid-plugins.item-lister.engine :as engine]))



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



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def request-id         engine/request-id)
(def resolver-id        engine/resolver-id)
(def new-item-uri       engine/new-item-uri)
(def add-new-item-event engine/add-new-item-event)
(def route-id           engine/route-id)
(def render-event       engine/render-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn get-downloaded-items
  ; @param (keyword) extension-id
  ;
  ; @return (maps in vector)
  [db [_ extension-id]]
  (get-in db [extension-id :lister-data]))

(defn get-downloaded-item-count
  ; @param (keyword) extension-id
  ;
  ; @return (integer)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (count downloaded-items)))

(defn get-all-item-count
  ; @param (keyword) extension-id
  ;
  ; @return (integer)
  [db [_ extension-id]]
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
           (return   0))))

(defn all-items-downloaded?
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [all-item-count        (r get-all-item-count        db extension-id)
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
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
  ; @param (keyword) extension-id
  ;
  ; @return (string)
  [db [_ extension-id]]
  (let [search-term  (get-in db [extension-id :lister-meta :search-term])]
       (str search-term)))

(defn get-order-by
  ; @param (keyword) extension-id
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :lister-meta :order-by] DEFAULT-ORDER-BY))

(defn- download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (request-id extension-id item-namespace)]
       (println (str extension-id))
       (println (str request-id))
       (println (str (not (r sync/request-sent?    db request-id))))
       (println (str (not (r all-items-downloaded? db extension-id))))
           ; XXX#0791
           ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
           ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
       (or (not (r sync/request-sent?    db request-id))
           (not (r all-items-downloaded? db extension-id)))))

(defn get-description
  ; @param (keyword) extension-id
  ;
  ; @return (string)
  [db [_ extension-id]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        all-item-count        (r get-all-item-count        db extension-id)]
       (components/content {:content      :npn-items-downloaded
                            :replacements [downloaded-item-count all-item-count]})))

(defn get-header-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r pattern/get-header-props db :my-extension)
  ;
  ; @return (map)
  ;  {:viewport-small? (boolean)}
  [db [_ extension-id]]
  {:search-mode?    (get-in db [extension-id :lister-meta :search-mode?])
   :viewport-small? (r environment/viewport-small? db)})

(defn get-view-props
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)}
  [db [_ extension-id]]
  {:description (r get-description db extension-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-lister/toggle-search-mode! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :lister-meta :search-mode?] not))

(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn- toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r item-lister/toggle-select-mode! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :lister-meta :select-mode?] not))

(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/search-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db       (-> db (dissoc-in [extension-id :lister-data])
                        (dissoc-in [extension-id :lister-meta :document-count]))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/order-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db       (-> db (dissoc-in [extension-id :lister-data])
                        (dissoc-in [extension-id :lister-meta :document-count]))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/receive-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [resolver-id    (resolver-id extension-id item-namespace)
            documents      (get-in server-response [resolver-id :documents])
            document-count (get-in server-response [resolver-id :document-count])]
           {:db       (-> db (update-in [extension-id :lister-data] vector/concat-items documents)
                             ; Szükséges frissíteni a keresési feltételeknek megfelelő
                             ; dokumentumok számát, mert megváltozhat az értéke!
                             (assoc-in  [extension-id :lister-meta :document-count] document-count))
            ; Az elemek letöltődése után, ha maradt még a szerveren letöltendő elem, akkor újratölti
            ; az infinite-loader komponenst, hogy megállapítsa, hogy az a viewport területén van-e még.
            :dispatch-if [(r download-more-items? db extension-id item-namespace)
                          [:x.app-tools/reload-infinite-loader! extension-id]]})))

(a/reg-event-fx
  :item-lister/request-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; A letöltött dokumentumok on-success helyett on-stalled időpontban kerülnek tárolásra
  ; a Re-Frame adatbázisba, így elkerülhető, hogy a request idle-timeout ideje alatt
  ; az újonnan letöltött dokumentumok már kirenderelésre kerüljenek, amíg a letöltést jelző
  ; felirat még megjelenik a lista végén.
  ;
  ; @usage
  ;  [:item-lister/request-items! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
      (if (r download-more-items? db extension-id item-namespace)
          (let [resolver-id    (resolver-id extension-id item-namespace)
                resolver-props {:downloaded-item-count (r get-downloaded-item-count db extension-id)
                                :search-term           (r get-search-term           db extension-id)
                                :order-by              (r get-order-by              db extension-id)}]
               [:x.app-sync/send-query! (request-id extension-id item-namespace)
                                        {:on-stalled [:item-lister/receive-items! extension-id item-namespace]
                                         :query      [`(~resolver-id ~resolver-props)]}]))))

(a/reg-event-fx
  :item-lister/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/load! :my-extension :my-type]
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db         (-> db (dissoc-in [extension-id :lister-data])
                          (dissoc-in [extension-id :lister-meta]))
       :dispatch-n [[:x.app-ui/listen-to-process! (request-id extension-id item-namespace)]
                    [:x.app-ui/set-header-title!  (param      extension-id)]
                    [:x.app-ui/set-window-title!  (param      extension-id)]
                    (render-event extension-id item-namespace)]}))
