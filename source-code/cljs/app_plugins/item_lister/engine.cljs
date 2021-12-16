
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



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
              [x.app-tools.api       :as tools]
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
(def DEFAULT-DOWNLOAD-LIMIT engine/DEFAULT-DOWNLOAD-LIMIT)
(def request-id             engine/request-id)
(def resolver-id            engine/resolver-id)
(def new-item-uri           engine/new-item-uri)
(def add-new-item-event     engine/add-new-item-event)
(def route-id               engine/route-id)
(def route-template         engine/route-template)
(def render-event           engine/render-event)



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

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r engine/no-items-to-show? db :my-namespace)
  ;
  ; @return (maps in vector)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (empty? downloaded-items)))

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
  ; megváltoztak (már nem nil, hanem true vagy false értékek).
  ;
  ; A checkbox input value-path Re-Frame adatábzis útvonalra írt true vagy false kimenete miatt
  ; szükséges térkép típusban tárolni a lista-elemek kiválasztottságának értékeit.
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

(defn- some-items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [received-count (get-in db [extension-id :lister-meta :received-count])]
       (not= received-count 0)))

(defn- download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
       ; XXX#0791
       ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
       ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
  (and (or (not (r synchronized?         db extension-id))
           (not (r all-items-downloaded? db extension-id)))
       ; BUG#7009
       (r some-items-received? db extension-id)))

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
  ;  {:actions-mode? (boolean)
  ;   :all-items-selected? (boolean)
  ;   :any-item-selected? (boolean)
  ;   :filter-mode? (boolean)
  ;   :no-items-to-show? (boolean)
  ;   :order-changed? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id]]
        ; If select-mode is enabled ...
  (cond (get-in db [extension-id :lister-meta :select-mode?])
        {:select-mode?        true
         :all-items-selected? (r all-items-selected? db extension-id)
         :any-item-selected?  (r any-item-selected?  db extension-id)}
        ; If search-mode is enabled ...
        (get-in db [extension-id :lister-meta :search-mode?])
        {:search-mode?     true}
        ; If reorder-mode is enabled ...
        (get-in db [extension-id :lister-meta :reorder-mode?])
        {:reorder-mode?    true
         :order-changed?   false}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?     true
         :filter            (get-in db [extension-id :lister-meta :filter])
         :filter-mode?      (get-in db [extension-id :lister-meta :filter-mode?])
         :no-items-to-show? (r no-items-to-show?           db extension-id)
         :viewport-small?   (r environment/viewport-small? db)}))

(a/reg-sub :item-lister/get-header-props get-header-props)

(defn- get-body-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:actions-mode? (boolean)
  ;   :downloaded-items (vector)
  ;   :disabled-items (integers in vector)
  ;   :no-items-to-show? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :synchronizing? (boolean)}
  [db [_ extension-id item-namespace]]
        ; If select-mode is enabled ...
  (cond (get-in db [extension-id :lister-meta :select-mode?])
        {:select-mode?      true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; If search-mode is enabled ...
        (get-in db [extension-id :lister-meta :search-mode?])
        {:search-mode?      true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; If reorder-mode is enabled ...
        (get-in db [extension-id :lister-meta :reorder-mode?])
        {:reorder-mode?     true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}
        ; Use actions-mode as default ...
        :default
        {:actions-mode?     true
         :downloaded-items  (r get-downloaded-items db extension-id)
         :no-items-to-show? (r no-items-to-show?    db extension-id)
         :synchronizing?    (r synchronizing?       db extension-id item-namespace)}))

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

(defn- reset-item-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :lister-data])
         (dissoc-in [extension-id :lister-meta :document-count])
         (dissoc-in [extension-id :lister-meta :synchronized?])))

(defn- quit-filter-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; WARNING#4569
  ; A {:filter-mode? true} az {:actions-mode? true} beállítással párhuzamosan fut, ezért
  ; az {:actions-mode? true} módból bármelyik másik módba átlépéskor szükséges a {:filter-mode? true}
  ; beállításból kilépni!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (dissoc-in [extension-id :lister-meta :filter-mode?])
         (dissoc-in [extension-id :lister-meta :filter])))

(defn- toggle-filter-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (-> db (update-in [extension-id :lister-meta :filter-mode?] not)
         (dissoc-in [extension-id :lister-meta :filter])))

(a/reg-event-db :item-lister/toggle-filter-mode! toggle-filter-mode!)

(defn- toggle-search-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :search-mode?] not)
             (r quit-filter-mode! % extension-id)))

(a/reg-event-db :item-lister/toggle-search-mode! toggle-search-mode!)

(defn- toggle-select-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :select-mode?] not)
             (dissoc-in % [extension-id :lister-meta :item-selections])
             (r quit-filter-mode! % extension-id)))

(a/reg-event-db :item-lister/toggle-select-mode! toggle-select-mode!)

(defn- toggle-reorder-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (as-> db % (update-in % [extension-id :lister-meta :reorder-mode?] not)
             (r quit-filter-mode! % extension-id)))

(a/reg-event-db :item-lister/toggle-reorder-mode! toggle-reorder-mode!)

(defn- select-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        item-selections       (reduce #(assoc %1 %2 true) {} (range 0 downloaded-item-count))]
       (assoc-in db [extension-id :lister-meta :item-selections] item-selections)))

(a/reg-event-db :item-lister/select-all-items! select-all-items!)

(defn- unselect-all-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (dissoc-in db [extension-id :lister-meta :item-selections]))

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

(a/reg-event-db :item-lister/toggle-item-selection! toggle-item-selection!)

(defn- mark-items-as-disabled!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r engine/mark-items-as-disabled! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id item-dexes]]
  (update-in db [extension-id :lister-meta :disabled-items]
             vector/concat-items item-dexes))

(defn- unmark-items-as-disabled!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (integers in vector) item-dexes
  ;
  ; @usage
  ;  (r engine/unmark-items-as-disabled! db :my-extension [0 1 4])
  ;
  ; @return (map)
  [db [_ extension-id item-dexes]]
  (update-in db [extension-id :lister-meta :disabled-items]
             vector/remove-items item-dexes))

(defn- delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (map)
  [db [_ extension-id]]
  (let [selected-items (r get-selected-items db extension-id)]
       (r mark-items-as-disabled! db extension-id selected-items)))

(defn- use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) filter-id
  ;
  ; @return (map)
  [db [_ extension-id item-namespace filter-id]]
  (assoc-in db [extension-id :lister-meta :filter] filter-id))

(defn- discard-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  (dissoc-in db [extension-id :lister-meta :filter]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/duplicate-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]))

(a/reg-event-fx
  :item-lister/delete-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      {:db (r delete-selected-items! db extension-id)}))
      ; TODO
      ; Ameddig a kommunikáció történik a szerverrel, addig az egész lista legyen disabled állapotban
      ; és az új elemek letöltése szüneteljen addig? (item-lister pause function!)

(a/reg-event-fx
  :item-lister/search-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
           ; BUG#8071
           ; Az item-lister alapállapotba állítása után az infinite-loader komponens
           ; azonnal újratöltené az elemeket az eddigi beállításokkal még mielőtt
           ; az :item-lister/request-items! esemény elkezdené letölteni az elemeket
           ; a megváltozott beállításokkal, ezért szükséges az infinite-loader komponenst
           ; paused állapotba állítani.
           ; Az :item-lister/receive-items! esemény újratölti az infinite-loader komponenst
           ; ezért nem szükséges annak paused állapotát visszaállítani!
      {:db (as-> db % (r reset-item-lister!           % extension-id)
                      (r tools/pause-infinite-loader! % extension-id))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/order-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
           ; BUG#8071
      {:db (as-> db % (r reset-item-lister!           % extension-id)
                      (r tools/pause-infinite-loader! % extension-id))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/discard-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace]]
           ; BUG#8071
      {:db (as-> db % (r reset-item-lister!           % extension-id)
                      (r tools/pause-infinite-loader! % extension-id)
                      (r discard-filter!              % extension-id))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/use-filter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (keyword) filter-id
  (fn [{:keys [db]} [_ extension-id item-namespace filter-id]]
           ; BUG#8071
      {:db (as-> db % (r reset-item-lister!           % extension-id)
                      (r tools/pause-infinite-loader! % extension-id)
                      (r use-filter!                  % extension-id item-namespace filter-id))
       :dispatch [:item-lister/request-items! extension-id item-namespace]}))

(a/reg-event-fx
  :item-lister/receive-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  (fn [{:keys [db]} [_ extension-id item-namespace server-response]]
      (let [resolver-id    (resolver-id extension-id item-namespace)
            documents      (get-in server-response [resolver-id :documents])
            document-count (get-in server-response [resolver-id :document-count])
            received-count (count documents)]
                           ; XXX#3907
                           ; Az item-lister a dokumentumokat névtér nélkül tárolja, így
                           ; a lista-elemek felsorolásakor és egyes Re-Frame feliratkozásokban
                           ; a dokumentumok egyes értékeinek olvasása kevesebb erőforrást igényel.
           {:db (as-> db % (r db/store-collection! % [extension-id :lister-data] documents {:remove-namespace? true})
                           ; A névtér nélkül tárolt dokumentumokon végzett műveletkhez egyes külső
                           ; moduloknak szüksége lehet a dokumentumok névterének ismeretére!
                           (assoc-in % [extension-id :lister-meta :item-namespace] item-namespace)
                           ; Szükséges frissíteni a keresési feltételeknek megfelelő
                           ; dokumentumok számát, mert megváltozhat az értéke!
                           (assoc-in % [extension-id :lister-meta :document-count] document-count)
                           ; Szükséges eltárolni, hogy megtörtént-e az első kommunikáció a szerverrel!
                           (assoc-in % [extension-id :lister-meta :synchronized?]  true)
                           ; BUG#7009
                           ; Ha a legutoljára letöltött dokumentumok száma 0, de a letöltött dokumentumok
                           ; száma kevesebb, mint a szerverről érkezett document-count érték, akkor
                           ; az elemek letöltésével kapcsolatban valamilyen hiba történt.
                           ; Az ilyen típusú hibák megállapításához szükséges a received-count
                           ; értéket eltárolni.
                           (assoc-in % [extension-id :lister-meta :received-count] received-count))
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
  (fn [{:keys [db]} [_ extension-id item-namespace]]
      ; Ha az infinite-loader komponens ismételten megjelenik a viewport területén, csak abban
      ; az esetben próbáljon újabb elemeket letölteni, ha még nincs az összes letöltve.
      (if (r download-more-items? db extension-id)
          (let [resolver-id    (resolver-id extension-id item-namespace)
                resolver-props {:downloaded-item-count (r get-downloaded-item-count db extension-id)
                                :search-term           (r get-search-term           db extension-id)
                                :order-by              (r get-order-by              db extension-id)
                                :download-limit        (get-in db [extension-id :lister-meta :download-limit])
                                :filter                (get-in db [extension-id :lister-meta :filter])}]
               [:sync/send-query! (request-id extension-id item-namespace)
                                  {:on-stalled [:item-lister/receive-items! extension-id item-namespace]
                                   :query      [`(~resolver-id ~resolver-props)]}]))))

(a/reg-event-fx
  :item-lister/load!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) lister-props
  ;  {:download-limit (integer)}
  (fn [{:keys [db]} [_ extension-id item-namespace lister-props]]
      {:db (-> db (dissoc-in [extension-id :lister-data])
                  (assoc-in  [extension-id :lister-meta] lister-props))
       :dispatch-n [[:ui/listen-to-process! (request-id extension-id item-namespace)]
                    [:ui/set-header-title!  (param      extension-id)]
                    [:ui/set-window-title!  (param      extension-id)]
                    (render-event extension-id item-namespace)]}))
