
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
              [mid-fruits.map        :as map :refer [dissoc-in]]
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
(def route-template     engine/route-template)
(def render-event       engine/render-event)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r engine/synchronizing? db :my-namespace :my-type)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn synchronized?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/synchronized? db :my-namespace)
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  ; A szerverrel való első kommunkáció megtörténtét, nem lehetséges az (r sync/request-sent? db ...)
  ; függvénnyel vizsgálni, mert ha az item-lister már meg volt jelenítve, akkor az újbóli
  ; megjelenítéskor (r sync/request-sent? db ...) függvény visszatérési értéke true lenne!
  (boolean (get-in db [extension-id :lister-meta :synchronized?])))

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-downloaded-items db :my-namespace)
  ;
  ; @return (maps in vector)
  [db [_ extension-id]]
  (get-in db [extension-id :lister-data]))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-downloaded-item-count db :my-namespace)
  ;
  ; @return (integer)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (count downloaded-items)))

(defn get-all-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-all-item-count db :my-namespace)
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
  ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
  (let [all-item-count (get-in db [extension-id :lister-meta :document-count])]
       (if (integer? all-item-count)
           (return   all-item-count)
           (return   0))))

(defn- get-item-selections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (r engine/get-item-selections :my-extension)
  ;  =>
  ;  {0 true 1 false 2 true 3 false ...}
  ;
  ; @return (map)
  [db [_ extension-id]]
  ; A lista-elemek kiválasztottságának értékei egy térképben vannak tárolva, ahol a kulcsok
  ; az egyes lista-elemek sorrendbeli pozíciói az értékek pedig a pozícióhoz tartozó lista-elem
  ; kiválasztottságának értékei.
  ; Csak azon lista-elemek kiválasztottságának értéke van eltárolva, amely értékek legalább egyszer
  ; megváltoztak (nem nil, hanem true vagy false).
  (get-in db [extension-id :lister-meta :item-selections]))

(defn- get-selected-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-selected-items :my-extension)
  ;
  ; @return (vector)
  [db [_ extension-id]]
  (let [item-selections (r get-item-selections db extension-id)]
       ; A lista-elemek kiválasztottságának listájából a true értékű pozíciók felsorolása egy vektorban:
       ; {0 true 1 false 2 true 3 false} => [0 2]
       (map/get-keys-by item-selections boolean)))

(defn- item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-selected? :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id _ item-dex]]
  (let [item-selections (r get-item-selections db extension-id)
        item-selection  (get item-selections item-dex)]
       (boolean item-selection)))

(defn- any-item-nonselected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [item-selections       (r get-item-selections       db extension-id)
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
           ; Ha a lista-elemek kiválasztottságának listája kevesebb elemet tartalmaz, mint
           ; az összes elemek száma, akkor biztos, hogy legalább egy elem nincs kiválasztva ...
       (or (< (count item-selections) downloaded-item-count)
           ; Ha a lista-elemek kiválasztottságának listája legalább egy false értéket tartalmaz,
           ; akkor biztos, hogy legalább egy elem nincs kiválasztva ...
           (map/any-value-match? item-selections not))))

(defn- all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  ; Kevesebb számítás szükséges a "legalább egy elem nincs kiválasztva" negálásához,
  ; mint az "összes elem ki van választva" megállapításához.
  (not (r any-item-nonselected? db extension-id)))

(defn- any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [item-selections (r get-item-selections db extension-id)]
       ; Ha a lista-elemek kiválasztottságának listája legalább egy true értéket tartalmaz,
       ; akkor biztos, hogy legalább egy elem ki van választva ...
       (map/any-value-match? item-selections boolean)))

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/all-items-downloaded? db :my-namespace)
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
       ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
       (>= downloaded-item-count all-item-count)))

(defn get-search-term
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-search-term db :my-namespace)
  ;
  ; @return (string)
  [db [_ extension-id]]
  (let [search-term  (get-in db [extension-id :lister-meta :search-term])]
       (str search-term)))

(defn get-order-by
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-order-by db :my-namespace)
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
  ; XXX#0791
  ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
  ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
  (or (not (r synchronized?         db extension-id))
      (not (r all-items-downloaded? db extension-id))))

(defn- get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/get-description db :my-extension)
  ;
  ; @return (string)
  [db [_ extension-id]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        all-item-count        (r get-all-item-count        db extension-id)]
       (components/content {:content      :npn-items-downloaded
                            :replacements [downloaded-item-count all-item-count]})))

(defn- get-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  ;  {:search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id]]
  (if-let [select-mode? (get-in db [extension-id :lister-meta :select-mode?])]
          ; If select-mode is enabled ...
          {:select-mode? true
           :all-items-selected? (r all-items-selected? db extension-id)
           :any-item-selected?  (r any-item-selected?  db extension-id)
           :search-mode?        (get-in db [extension-id :lister-meta :search-mode?])
           :viewport-small?     (r environment/viewport-small? db)}
          ; If select-mode is NOT enabled ...
          {:search-mode?        (get-in db [extension-id :lister-meta :search-mode?])
           :viewport-small?     (r environment/viewport-small? db)}))

(a/reg-sub :item-lister/get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:downloaded-items (vector)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
  {:downloaded-items (r get-downloaded-items db extension-id item-namespace)
   :synchronizing?   (r synchronizing?       db extension-id item-namespace)
   :select-mode?     (get-in db [extension-id :lister-meta :select-mode?])})

(a/reg-sub :item-lister/get-body-props get-body-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)}
  [db [_ extension-id]]
  {:description (r get-description db extension-id)})

(a/reg-sub :item-lister/get-view-props get-view-props)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/toggle-search-mode! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (update-in db [extension-id :lister-meta :search-mode?] not))

; @usage
;  [:item-lister/toggle-search-mode! :my-extension]
(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn- toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/toggle-select-mode! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (update-in [extension-id :lister-meta :select-mode?] not)
         (dissoc-in [extension-id :lister-meta :item-selections])))

; @usage
;  [:item-lister/toggle-select-mode! :my-extension]
(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

(defn- select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/select-all-items! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        item-selections       (reduce #(assoc %1 %2 true) {} (range 0 downloaded-item-count))]
       (assoc-in db [extension-id :lister-meta :item-selections] item-selections)))

; @usage
;  [:item-lister/select-all-items! :my-extension]
(a/reg-event-db :item-lister/select-all-items! select-all-items!)

(defn- unselect-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/unselect-all-items! :my-extension)
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :lister-meta :item-selections]))

; @usage
;  [:item-lister/unselect-all-items! :my-extension]
(a/reg-event-db :item-lister/unselect-all-items! unselect-all-items!)

(defn- toggle-item-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/toggle-item-selection! :my-extension :my-type 0)
  ;
  ; @return (map)
  [db [_ extension-id _ item-dex]]
  (update-in db [extension-id :lister-meta :item-selections item-dex] not))

; @usage
;  [:item-lister/toggle-item-selection! :my-extension]
(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [:item-lister/search-items! :my-extension :my-type]
  (fn [_ [_ extension-id item-namespace]]))

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
                             (assoc-in  [extension-id :lister-meta :document-count] document-count)
                             ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
                             (assoc-in  [extension-id :lister-meta :synchronized?]  true))
            ; Az elemek letöltődése után újratölti az infinite-loader komponenst, hogy megállapítsa,
            ; hogy az a viewport területén van-e még.
            :dispatch [:tools/reload-infinite-loader! extension-id]})))

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
               [:sync/send-query! (request-id extension-id item-namespace)
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
       :dispatch-n [[:ui/listen-to-process! (request-id extension-id item-namespace)]
                    [:ui/set-header-title!  (param      extension-id)]
                    [:ui/set-window-title!  (param      extension-id)]
                    (render-event extension-id item-namespace)]}))
