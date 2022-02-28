
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.subs
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.logical    :refer [nor]]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-environment.api :as environment]
              [x.app-router.api      :as router]
              [x.app-sync.api        :as sync]
              [app-plugins.item-lister.engine :as engine]
              [mid-plugins.item-lister.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.subs
(def get-lister-props subs/get-lister-props)
(def get-meta-item    subs/get-meta-item)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (maps in vector)
  [db [_ extension-id _]]
  (get-in db [extension-id :item-lister/data-items]))

(defn items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; XXX#0499
  ; A szerverrel való első kommunkáció megtörténtét, nem lehetséges az (r sync/request-sent? db ...)
  ; függvénnyel vizsgálni, mert ha az item-lister már meg volt jelenítve, akkor az újbóli
  ; megjelenítéskor (r sync/request-sent? db ...) függvény visszatérési értéke true lenne!
  (let [items-received? (r get-meta-item db extension-id item-namespace :items-received?)]
       (boolean items-received?)))

(defn synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [request-id (engine/request-id extension-id item-namespace)]
       (r sync/listening-to-request? db request-id)))

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [downloaded-items (r get-downloaded-items db extension-id item-namespace)]
       (empty? downloaded-items)))

(defn any-item-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [downloaded-items (r get-downloaded-items db extension-id item-namespace)]
       (vector/nonempty? downloaded-items)))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  (let [downloaded-items (r get-downloaded-items db extension-id item-namespace)]
       (count downloaded-items)))

(defn get-all-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  ; - Ha a tárolt érték nil, akkor a visszatérési érték 0
  ; - Ha a szerver hibásan nil értéket küld le, akkor a 0 visszatérési érték miatt
  ;   az all-items-downloaded? függvény visszatérési értéke true lesz ezért megáll
  ;   az újabb elemek letöltése.
  ; - Hibás szerver-működés esetén szükséges, hogy az infinite-loader komponens
  ;   ne próbálja újra és újra letölteni a további feltételezett elemeket.
  ; - XXX#0499
  ;   Ha még nem történt meg az első kommunikáció a szerverrel, akkor a get-all-item-count
  ;   függvény visszatérési értéke nem tekinthető mérvadónak!
  ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
  (let [all-item-count (r get-meta-item db extension-id item-namespace :document-count)]
       (if (integer? all-item-count)
           (return   all-item-count)
           (return   0))))

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [       all-item-count (r        get-all-item-count db extension-id item-namespace)
        downloaded-item-count (r get-downloaded-item-count db extension-id item-namespace)]
       ; XXX#0791
       ; - = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
       ;   nagyobb a downloaded-item-count értéke, mint az all-item-count értéke,
       ;   akkor ne próbáljon további feltételezett elemeket letölteni.
       ; - XXX#0499
       ;   Ha még nem történt meg az első kommunikáció a szerverrel, akkor az all-items-downloaded?
       ;   függvény visszatérési értéke nem tekinthető mérvadónak!
       ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
       (>= downloaded-item-count all-item-count)))

(defn no-items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [received-count (r get-meta-item db extension-id item-namespace :received-count)]
       (= received-count 0)))

(defn download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (and ; XXX#0499
       ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
       ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
       (or (not (r items-received?       db extension-id item-namespace))
           (not (r all-items-downloaded? db extension-id item-namespace)))
       ; BUG#7009
       (not (r no-items-received? db extension-id item-namespace))))

(defn request-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (and ; BUG#4506
       ; Ha a keresőmezőbe írsz egy karaktert, akkor meg az on-type-ended esemény,
       ; és ha még a mező {:disabled? true} állapotba lépése előtt megnyomod az ESC billentyűt,
       ; akkor megtörténik az on-empty esemény is ezért a lekérés indítása kétszer történne meg!
       ; Ezért szükséges vizsgálni a synchronizing? függvény kimenetét, hogy ha már elindult
       ; az első lekérés, akkor több ne induljon, amíg az első be nem fejeződik!
            (r download-more-items? db extension-id item-namespace)
       (not (r synchronizing?       db extension-id item-namespace))))

(defn downloading-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  ; A kiválasztott elemeken végzett műveletek is {:synchronizing? true} állapotba hozzák
  ; az item-lister plugint, ezért szükséges megkülönböztetni az elemek letöltése szinkronizációt,
  ; az elemeken végzett műveletek szinkronizációval.
  (and (r download-more-items? db extension-id item-namespace)
       (r synchronizing?       db extension-id item-namespace)))

(defn lister-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [items-received? (r items-received? db extension-id item-namespace)
        synchronizing?  (r synchronizing?  db extension-id item-namespace)]
       ; XXX#3219
       (or synchronizing? (not items-received?))))

(defn route-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [route-id (r router/get-current-route-id db)]
       (= route-id (engine/route-id extension-id item-namespace))))

(defn items-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [item-actions (r get-meta-item db extension-id item-namespace :item-actions)]
       (vector/nonempty? item-actions)))

(defn items-sortable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :sortable?))

(defn set-title?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r route-handled? db extension-id item-namespace))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id item-namespace)
        all-item-count        (r get-all-item-count        db extension-id item-namespace)
        items-received?       (r items-received?           db extension-id item-namespace)]
       (if items-received? (components/content {:content      :npn-items-downloaded
                                                :replacements [downloaded-item-count all-item-count]}))))

(defn get-item-actions
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (keywords in vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :item-actions))

(defn get-new-item-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :new-item-options))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :error-mode?))

(defn menu-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [reorder-mode? (r get-meta-item db extension-id item-namespace :reorder-mode?)
        search-mode?  (r get-meta-item db extension-id item-namespace :search-mode?)]
       (nor reorder-mode? search-mode?)))

(defn reorder-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :reorder-mode?))

(defn search-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :search-mode?))

(defn select-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :select-mode?))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-disabled-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :disabled-items))

(defn item-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-disabled? db :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace item-dex]]
  (let [disabled-items (r get-disabled-items db extension-id item-namespace)]
       (vector/contains-item? disabled-items item-dex)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-item-dexes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integers in vector)
  [db [_ extension-id item-namespace]]
  (r get-meta-item db extension-id item-namespace :selected-items))

(defn get-selected-item-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (strings in vector)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r get-meta-item db extension-id item-namespace :selected-items)]
       (letfn [(f [result item-dex]
                  (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])]
                       (conj result item-id)))]
              (reduce f [] selected-items))))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (integer)
  [db [_ extension-id item-namespace]]
  (let [selected-items (r get-selected-item-dexes db extension-id item-namespace)]
       (count selected-items)))

(defn item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/item-selected? db :my-extension :my-type 0)
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace item-dex]]
  (let [selected-item-dexes (r get-selected-item-dexes db extension-id item-namespace)]
       (vector/contains-item? selected-item-dexes item-dex)))

(defn all-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-items-count  (r get-selected-item-count   db extension-id item-namespace)
        downloaded-item-count (r get-downloaded-item-count db extension-id item-namespace)]
       (and (not= downloaded-item-count 0)
            (= selected-items-count downloaded-item-count))))

(defn any-item-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-item-dexes (r get-selected-item-dexes db extension-id item-namespace)]
       (vector/nonempty? selected-item-dexes)))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [selected-item-dexes (r get-selected-item-dexes db extension-id item-namespace)]
       (-> selected-item-dexes vector/nonempty? not)))

(defn toggle-item-selection?
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r item-lister/toggle-item-selection? db :my-extension :my-type 42)
  [db [_ extension-id item-namespace item-dex]]
  (and ; XXX#5660
       ; A SHIFT billentyű lenyomása közben az elemre kattintva az elem, hozzáadódik a kijelölt elemek listájához.
            (r items-selectable?        db extension-id item-namespace)
            (r environment/key-pressed? db 16)
       (not (r lister-disabled?         db extension-id item-namespace))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-filter-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  ;  {:$and (maps in vector)}
  [db [_ extension-id item-namespace]]
  (let [active-filter (r get-meta-item db extension-id item-namespace :active-filter)
            prefilter (r get-meta-item db extension-id item-namespace     :prefilter)]
       (cond-> {} active-filter (update :$and vector/conj-item active-filter)
                      prefilter (update :$and vector/conj-item     prefilter))))

(defn get-search-term
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [search-term (r get-meta-item db extension-id item-namespace :search-term)]
       (str search-term)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-backup-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (strings in vector) item-ids
  ;
  ; @return (namespaced maps in vector)
  [db [_ extension-id item-namespace item-ids]]
  (vector/->items item-ids #(let [backup-item (get-in db [extension-id :item-lister/backup-items %])]
                                 (db/document->namespaced-document backup-item item-namespace))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-changed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (return false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/all-items-downloaded? :my-extension :my-type]
(a/reg-sub :item-lister/all-items-downloaded? all-items-downloaded?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/downloading-items? :my-extension :my-type]
(a/reg-sub :item-lister/downloading-items? downloading-items?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-downloaded-items :my-extension :my-type]
(a/reg-sub :item-lister/get-downloaded-items get-downloaded-items)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/items-received? :my-extension :my-type]
(a/reg-sub :item-lister/items-received? items-received?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/no-items-to-show? :my-extension :my-type]
(a/reg-sub :item-lister/no-items-to-show? no-items-to-show?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/all-items-selected? :my-extension :my-type]
(a/reg-sub :item-lister/all-items-selected? all-items-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/any-item-selected? :my-extension :my-type]
(a/reg-sub :item-lister/any-item-selected? any-item-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/no-items-selected? :my-extension :my-type]
(a/reg-sub :item-lister/no-items-selected? no-items-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/order-changed? :my-extension :my-type]
(a/reg-sub :item-lister/order-changed? order-changed?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-description :my-extension :my-type]
(a/reg-sub :item-lister/get-description get-description)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-item-actions :my-extension :my-type]
(a/reg-sub :item-lister/get-item-actions get-item-actions)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/get-new-item-options :my-extension :my-type]
(a/reg-sub :item-lister/get-new-item-options get-new-item-options)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/lister-disabled? :my-extension :my-type]
(a/reg-sub :item-lister/lister-disabled? lister-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-disabled? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-disabled? item-disabled?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
; @param (integer) item-dex
;
; @usage
;  [:item-lister/item-selected? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-selected? item-selected?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/items-selectable? :my-extension :my-type]
(a/reg-sub :item-lister/items-selectable? items-selectable?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/items-sortable? :my-extension :my-type]
(a/reg-sub :item-lister/items-sortable? items-sortable?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/error-mode? :my-extension :my-type]
(a/reg-sub :item-lister/error-mode? error-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/menu-mode? :my-extension :my-type]
(a/reg-sub :item-lister/menu-mode? menu-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/reorder-mode? :my-extension :my-type]
(a/reg-sub :item-lister/reorder-mode? reorder-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/search-mode? :my-extension :my-type]
(a/reg-sub :item-lister/search-mode? search-mode?)

; @param (keyword) extension-id
; @param (keyword) item-namespace
;
; @usage
;  [:item-lister/select-mode? :my-extension :my-type]
(a/reg-sub :item-lister/select-mode? select-mode?)
