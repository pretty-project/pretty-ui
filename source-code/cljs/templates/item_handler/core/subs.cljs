
(ns templates.item-handler.core.subs
    (:require [engines.item-handler.core.subs     :as core.subs]
              [engines.item-handler.download.subs :as download.subs]
              [re-frame.api                       :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.item-handler.core.subs
(def get-meta-item          core.subs/get-meta-item)
(def handler-synchronizing? core.subs/handler-synchronizing?)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handler-disabled?
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  ; XXX#3219
  ; Azért szükséges vizsgálni az {:data-received? ...} tulajdonság értékét, hogy
  ; a kezelő {:disabled? true} állapotban legyen, amíg még NEM kezdődött el az
  ; adatok letöltése!
  (let [data-received?         (r download.subs/data-received? db handler-id)
        handler-synchronizing? (r handler-synchronizing?       db handler-id)]
       (or handler-synchronizing? (not data-received?))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn display-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) handler-id
  ;
  ; @return (boolean)
  [db [_ handler-id]]
  (r get-meta-item db handler-id :engine-error))

(defn display-ghost?
  ; @param (keyword) handler-id
  ; @param (map) body-props
  ; {:item-id (string)
  ;  :items-path (vector)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [item-id items-path]}]]
  ; Before the 'downloader' component mounted into the React tree, the current
  ; item's ID and the downloaded items' path doesn't available in the Re-Frame DB,
  ; therefore the 'display-ghost?' function uses the body-props map's 'item-id' and
  ; 'items-path' properties to determine whether the current item is downloaded.
  (let [downloaded-item (get-in db (conj items-path item-id))]
       (nil? downloaded-item)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) handler-id
(r/reg-sub :item-handler/handler-disabled? handler-disabled?)

; @param (keyword) handler-id
(r/reg-sub :item-handler/display-error? display-error?)

; @param (keyword) handler-id
(r/reg-sub :item-handler/display-ghost? display-ghost?)
