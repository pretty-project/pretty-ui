
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.subs
    (:require [mid-fruits.candy                 :refer [return]]
              [mid-fruits.vector                :as vector]
              [plugins.item-lister.mount.subs   :as mount.subs]
              [plugins.plugin-handler.core.subs :as core.subs]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def plugin-synchronizing? core.subs/plugin-synchronizing?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r core.subs/get-request-id db lister-id :lister))

(defn lister-synchronizing?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (r plugin-synchronizing? db lister-id :lister))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (maps in vector)
  [db [_ lister-id]]
  (let [items-path (r mount.subs/get-body-prop db lister-id :items-path)]
       (get-in db items-path)))

(defn items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; XXX#0499
  ; A szerverrel való első kommunkáció megtörténtét, nem lehetséges az (r sync/request-sent? db ...)
  ; függvénnyel vizsgálni, mert ha az item-lister már meg volt jelenítve, akkor az újbóli
  ; megjelenítéskor (r sync/request-sent? db ...) függvény visszatérési értéke true lenne!
  (let [items-received? (r get-meta-item db lister-id :items-received?)]
       (boolean items-received?)))

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [downloaded-items (r get-downloaded-items db lister-id)]
       (empty? downloaded-items)))

(defn any-item-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [downloaded-items (r get-downloaded-items db lister-id)]
       (vector/nonempty? downloaded-items)))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (integer)
  [db [_ lister-id]]
  (let [downloaded-items (r get-downloaded-items db lister-id)]
       (count downloaded-items)))

(defn get-all-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (integer)
  [db [_ lister-id]]
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
  (let [all-item-count (r get-meta-item db lister-id :document-count)]
       (if (integer? all-item-count)
           (return   all-item-count)
           (return   0))))

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [       all-item-count (r        get-all-item-count db lister-id)
        downloaded-item-count (r get-downloaded-item-count db lister-id)]
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
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [received-count (r get-meta-item db lister-id :received-count)]
       (= received-count 0)))

(defn download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (and ; XXX#0499
       ; Ha még nem történt meg az első kommunikáció a szerverrel, akkor
       ; az all-items-downloaded? függvény visszatérési értéke nem tekinthető mérvadónak!
       (or (not (r items-received?       db lister-id))
           (not (r all-items-downloaded? db lister-id)))
       ; BUG#7009
       (not (r no-items-received? db lister-id))))

(defn request-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (and ; BUG#4506
       ; Ha a keresőmezőbe írsz egy karaktert, akkor meg az on-type-ended esemény,
       ; és ha még a mező {:disabled? true} állapotba lépése előtt megnyomod az ESC billentyűt,
       ; akkor megtörténik az on-empty esemény is ezért a lekérés indítása kétszer történne meg!
       ; Ezért szükséges vizsgálni a lister-synchronizing? függvény kimenetét, hogy ha már elindult
       ; az első lekérés, akkor több ne induljon, amíg az első be nem fejeződik!
            (r download-more-items?  db lister-id)
       (not (r lister-synchronizing? db lister-id))))

(defn downloading-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; A kiválasztott elemeken végzett műveletek is {:lister-synchronizing? true} állapotba hozzák
  ; az item-lister plugint, ezért szükséges megkülönböztetni az elemek letöltése szinkronizációt,
  ; az elemeken végzett műveletek szinkronizációval.
  (and (r download-more-items?  db lister-id)
       (r lister-synchronizing? db lister-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn lister-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [items-received?       (r items-received?       db lister-id)
        lister-synchronizing? (r lister-synchronizing? db lister-id)]
       ; XXX#3219
       (or lister-synchronizing? (not items-received?))))

(defn items-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; Az items-selectable? függvényre a header komponens nélkül használt item-lister plugin esetén
  ; is szükség van, ezért az {:item-actions [...]} tulajdonság a body komponens paramtére.
  (let [item-actions (r mount.subs/get-body-prop db lister-id :item-actions)]
       (vector/nonempty? item-actions)))

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (metamorphic-content)
  [db [_ lister-id]]
  (let [downloaded-item-count (r get-downloaded-item-count db lister-id)
        all-item-count        (r get-all-item-count        db lister-id)
        items-received?       (r items-received?           db lister-id)]
       (if items-received? (components/content {:content      :npn-items-downloaded
                                                :replacements [downloaded-item-count all-item-count]}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-filter-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  ;  {:$and (maps in vector)}
  [db [_ lister-id]]
  (let [active-filter (r get-meta-item db lister-id :active-filter)
            prefilter (r get-meta-item db lister-id     :prefilter)]
       (cond-> {} active-filter (update :$and vector/conj-item active-filter)
                      prefilter (update :$and vector/conj-item     prefilter))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
; @param (keyword) item-key
;
; @usage
;  [:item-lister/get-meta-item :my-lister :my-item]
(a/reg-sub :item-lister/get-meta-item get-meta-item)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/all-items-downloaded? :my-lister]
(a/reg-sub :item-lister/all-items-downloaded? all-items-downloaded?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/get-downloaded-items :my-lister]
(a/reg-sub :item-lister/get-downloaded-items get-downloaded-items)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/items-received? :my-lister]
(a/reg-sub :item-lister/items-received? items-received?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/no-items-to-show? :my-lister]
(a/reg-sub :item-lister/no-items-to-show? no-items-to-show?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/downloading-items? :my-lister]
(a/reg-sub :item-lister/downloading-items? downloading-items?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/get-description :my-lister]
(a/reg-sub :item-lister/get-description get-description)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/lister-disabled? :my-lister]
(a/reg-sub :item-lister/lister-disabled? lister-disabled?)

; @param (keyword) lister-id
;
; @usage
;  [:item-lister/items-selectable? :my-lister]
(a/reg-sub :item-lister/items-selectable? items-selectable?)
