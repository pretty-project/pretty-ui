
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.subs
    (:require [candy.api                         :refer [return]]
              [engines.engine-handler.core.subs  :as core.subs]
              [engines.item-lister.body.subs     :as body.subs]
              [engines.item-lister.download.subs :as download.subs]
              [re-frame.api                      :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-meta-item         core.subs/get-meta-item)
(def engine-synchronizing? core.subs/engine-synchronizing?)
(def get-item-path         core.subs/get-item-path)
(def get-downloaded-item   core.subs/get-downloaded-item)
(def get-item-order        core.subs/get-item-order)
(def get-listed-items      core.subs/get-listed-items)
(def get-listed-item-count core.subs/get-listed-item-count)
(def export-listed-items   core.subs/export-listed-items)
(def item-listed?          core.subs/item-listed?)
(def use-query-prop        core.subs/use-query-prop)
(def use-query-params      core.subs/use-query-params)



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
  (r engine-synchronizing? db lister-id :lister))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn no-items-to-show?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [item-order (r get-item-order db lister-id)]
       (empty? item-order)))

(defn get-all-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (integer)
  [db [_ lister-id]]
  (if-let [all-item-count (r get-meta-item db lister-id :all-item-count)]
          (return all-item-count)
          (return 0)))

(defn all-items-downloaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; XXX#0791
  ; = vizsgálat helyett szükséges >= vizsgálatot alkalmazni, hogy ha hibásan
  ; nagyobb a downloaded-item-count értéke, mint az all-item-count értéke,
  ; akkor ne próbáljon további feltételezett elemeket letölteni.
  (let [   all-item-count (r    get-all-item-count        db lister-id)
        listed-item-count (r get-listed-item-count        db lister-id)
        data-received?    (r download.subs/data-received? db lister-id)]
       (and data-received? (>= listed-item-count all-item-count))))

(defn no-items-received?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  (let [received-item-count (r get-meta-item db lister-id :received-item-count)]
       (= received-item-count 0)))

(defn download-more-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (boolean)
  [db [_ lister-id]]
  ; BUG#7009
  (and (not (r all-items-downloaded? db lister-id))
       (not (r no-items-received?    db lister-id))))

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
  ; az item-lister engine-t, ezért önmagában a {:lister-synchronizing? true} állapot nem
  ; feltétlenül azt jelenti, hogy elemek letöltése történik. Ezért szükséges az elemek letöltése
  ; állapot megállapításához a lister-synchronizing? és download-more-items? függvényt
  ; együtt alkalmazni.
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
  (let [data-received?        (r download.subs/data-received? db lister-id)
        lister-synchronizing? (r lister-synchronizing?        db lister-id)]
       ; XXX#3219
       (or lister-synchronizing? (not data-received?))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-filter-pattern
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (map)
  [db [_ lister-id]]
  ; Az {:active-filter ...} tulajdonság beállítása után az item-lister figyelmen kívül
  ; hagyja a {:prefilter ...} tulajdonságként átadott szűrési feltételeket az elemek letöltésekor.
  (or (r get-meta-item           db lister-id :active-filter)
      (r body.subs/get-body-prop db lister-id :prefilter)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-current-order-by
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @return (namespaced keyword)
  [db [_ lister-id]]
  (r get-meta-item db lister-id :order-by))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) lister-id
; @param (keyword) item-key
;
; @usage
; [:item-lister/get-meta-item :my-lister :my-item]
(r/reg-sub :item-lister/get-meta-item get-meta-item)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/get-listed-item-count :my-lister]
(r/reg-sub :item-lister/get-listed-item-count get-listed-item-count)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/all-items-downloaded? :my-lister]
(r/reg-sub :item-lister/all-items-downloaded? all-items-downloaded?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/get-listed-items :my-lister]
(r/reg-sub :item-lister/get-listed-items get-listed-items)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/get-all-item-count :my-lister]
(r/reg-sub :item-lister/get-all-item-count get-all-item-count)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/no-items-to-show? :my-lister]
(r/reg-sub :item-lister/no-items-to-show? no-items-to-show?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/downloading-items? :my-lister]
(r/reg-sub :item-lister/downloading-items? downloading-items?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/lister-disabled? :my-lister]
(r/reg-sub :item-lister/lister-disabled? lister-disabled?)

; @param (keyword) lister-id
;
; @usage
; [:item-lister/get-current-order-by :my-lister]
(r/reg-sub :item-lister/get-current-order-by get-current-order-by)
