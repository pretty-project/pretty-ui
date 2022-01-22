
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.7.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.subs
    (:require [mid-fruits.candy      :refer [param return]]
              [mid-fruits.keyword    :as keyword]
              [mid-fruits.vector     :as vector]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-environment.api :as environment]
              [x.app-sync.api        :as sync]
              [app-plugins.item-lister.engine :as engine]
              [mid-plugins.item-lister.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.subs
(def get-meta-item subs/get-meta-item)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-downloaded-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (maps in vector)
  [db [_ extension-id]]
  (get-in db [extension-id :item-lister/data-items]))

(a/reg-sub :item-lister/get-downloaded-items get-downloaded-items)

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

(defn lister-disabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (or ; Azért szükséges vizsgálni az {:items-received? ...} tulajdonság értékét, hogy
      ; a listázó {:disabled? true} állapotban legyen, amíg NEM kezdődött még el
      ; a szinkronizálás!
           (r get-meta-item   db extension-id item-namespace :error-mode?)
           (r synchronizing?  db extension-id item-namespace)
      (not (r items-received? db extension-id item-namespace))))

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (empty? downloaded-items)))

(defn any-item-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
       (vector/nonempty? downloaded-items)))

(defn get-downloaded-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (integer)
  [db [_ extension-id]]
  (let [downloaded-items (r get-downloaded-items db extension-id)]
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

; @usage
;  [:item-lister/item-disabled? :my-extension :my-type 0]
(a/reg-sub :item-lister/item-disabled? item-disabled?)

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
       (reduce (fn [result item-dex]
                   (let [item-id (get-in db [extension-id :item-lister/data-items item-dex :id])]
                        (conj result item-id)))
               [] selected-items)))

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
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
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

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (boolean)
  [db [_ extension-id item-namespace]]
  (let [all-item-count        (r get-all-item-count        db extension-id item-namespace)
        downloaded-item-count (r get-downloaded-item-count db extension-id)]
       ; XXX#0791
       ; - = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
       ;   nagyobb a downloaded-item-count értéke, mint az all-item-count értéke,
       ;   akkor ne próbáljon további feltételezett elemeket letölteni.
       ; - XXX#0499
       ;   Ha még nem történt meg az első kommunikáció a szerverrel, akkor az all-items-downloaded?
       ;   függvény visszatérési értéke nem tekinthető mérvadónak!
       ;   Ezért az első kommunikáció megtörténtét szükséges külön vizsgálni!
       (>= downloaded-item-count all-item-count)))

(defn get-search-term
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (string)
  [db [_ extension-id item-namespace]]
  (let [search-term  (r get-meta-item db extension-id item-namespace :search-term)]
       (str search-term)))

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

(defn get-description
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (metamorphic-content)
  [db [_ extension-id item-namespace]]
  (let [downloaded-item-count (r get-downloaded-item-count db extension-id)
        all-item-count        (r get-all-item-count        db extension-id item-namespace)
        items-received?       (r items-received?           db extension-id item-namespace)]
       (if items-received? (components/content {:content      :npn-items-downloaded
                                                :replacements [downloaded-item-count all-item-count]}))))

(defn get-checkbox-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (integer) item-dex
  ;
  ; @usage
  ;  (r engine/get-checkbox-props db :my-extension :my-type 0)
  ;
  ; @return (map)
  [db [_ extension-id item-namespace item-dex]]
  {:item-selected?   (r item-selected?   db extension-id item-namespace item-dex)
   :lister-disabled? (r lister-disabled? db extension-id item-namespace)
   :select-mode?     (r get-meta-item    db extension-id item-namespace :select-mode?)})

(a/reg-sub :item-lister/get-checkbox-props get-checkbox-props)

(defn get-indicator-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @return (map)
  [db [_ extension-id item-namespace]]
  {:all-items-downloaded? (r all-items-downloaded? db extension-id item-namespace)
   :downloading-items?    (r downloading-items?    db extension-id item-namespace)
   :items-received?       (r items-received?       db extension-id item-namespace)
   :no-items-to-show?     (r no-items-to-show?     db extension-id)})

(a/reg-sub :item-lister/get-indicator-props get-indicator-props)

(defn get-header-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-lister/get-header-props :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:all-items-selected? (boolean)
  ;   :any-item-selected? (boolean)
  ;   :lister-disabled? (boolean)
  ;   :error-mode? (boolean)
  ;   :menu-mode? (boolean)
  ;   :no-items-to-show? (boolean)
  ;   :order-changed? (boolean)
  ;   :reorder-mode? (boolean)
  ;   :search-mode? (boolean)
  ;   :select-mode? (boolean)
  ;   :viewport-small? (boolean)}
  [db [_ extension-id item-namespace]]
  (cond ; If error-mode is enabled ...
        (r get-meta-item db extension-id item-namespace :error-mode?)
        {:lister-disabled? true
         :error-mode?      true
         :menu-mode?       true}
        ; If select-mode is enabled ...
        (r get-meta-item db extension-id item-namespace :select-mode?)
        {:select-mode? true
         :all-items-selected? (r all-items-selected? db extension-id item-namespace)
         :any-item-selected?  (r any-item-selected?  db extension-id item-namespace)
         :lister-disabled?    (r lister-disabled?    db extension-id item-namespace)}
        ; If search-mode is enabled ...
        (r get-meta-item db extension-id item-namespace :search-mode?)
        {:search-mode? true
         :lister-disabled? (r lister-disabled? db extension-id item-namespace)}
        ; If reorder-mode is enabled ...
        (r get-meta-item db extension-id item-namespace :reorder-mode?)
        {:reorder-mode?  true
         :order-changed? false
         :lister-disabled? (r lister-disabled? db extension-id item-namespace)}
        ; Use menu-mode as default ...
        :default
        {:menu-mode? true
         :lister-disabled?  (r lister-disabled?            db extension-id item-namespace)
         :no-items-to-show? (r no-items-to-show?           db extension-id)
         :viewport-small?   (r environment/viewport-small? db)}))

; @usage
;  [:item-lister/get-header-props :my-extension :my-type]
(a/reg-sub :item-lister/get-header-props get-header-props)

(defn get-body-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-lister/get-body-props :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:error-mode? (boolean)
  ;   :reorder-mode? (boolean)}
  [db [_ extension-id item-namespace]]
  (cond (r get-meta-item db extension-id item-namespace :error-mode?)   {:error-mode?   true}
        (r get-meta-item db extension-id item-namespace :reorder-mode?) {:reorder-mode? true}))

; @usage
;  [:item-lister/get-body-props :my-extension :my-type]
(a/reg-sub :item-lister/get-body-props get-body-props)

(defn get-view-props
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  (r item-lister/get-view-props :my-extension :my-type)
  ;
  ; @return (map)
  ;  {:description (metamorphic-content)
  ;   :error-mode? (boolean)}
  [db [_ extension-id item-namespace]]
  (if-let [error-mode? (r get-meta-item db extension-id item-namespace :error-mode?)]
          {:error-mode? true}
          {:description (r get-description db extension-id item-namespace)}))

; @usage
;  [:item-lister/get-view-props :my-extension :my-type]
(a/reg-sub :item-lister/get-view-props get-view-props)
