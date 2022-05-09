
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.events
    (:require [extensions.storage.media-selector.subs :as media-selector.subs]
              [mid-fruits.vector                      :as vector]
              [x.app-core.api                         :as a :refer [r]]
              [x.app-ui.api                           :as ui]
              [x.app-media.api                        :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unselect-file!
  [db [_ {:keys [filename]}]]
  (let [file-uri (media/filename->media-storage-uri filename)]
       (update-in db [:storage :media-selector/selected-items] vector/remove-item file-uri)))

(defn select-file!
  [db [_ {:keys [filename]}]]
  (let [file-uri (media/filename->media-storage-uri filename)]
       (if-let [multiple? (get-in db [:storage :media-selector/selector-props :multiple?])]
               (update-in db [:storage :media-selector/selected-items] vector/conj-item-once file-uri)
               (assoc-in  db [:storage :media-selector/selected-items] [file-uri]))))

(defn toggle-file-selection!
  [db [_ file-item]]
  (if (r media-selector.subs/file-selected? db file-item)
      (r unselect-file!                     db file-item)
      (r select-file!                       db file-item)))

(defn save-selected-items!
  [db _]
  ; XXX#8073
  ; A save-selected-items! függvény a {:value-path [...]} tulajdonságként átadott útvonalra
  ; a kiválasztott elem(ek)et, ...
  ; ... {:multiple? true}  beállítás használatával vektor típusként tárolja.
  ; ... {:multiple? false} beállítás használatával string típusként tárolja.
  (let [value-path (get-in db [:storage :media-selector/selector-props :value-path])]
       (if-let [multiple? (get-in db [:storage :media-selector/selector-props :multiple?])]
               (let [selected-items (get-in db [:storage :media-selector/selected-items])]
                    (assoc-in db value-path selected-items))
               (let [selected-item (get-in db [:storage :media-selector/selected-items 0])]
                    (assoc-in db value-path selected-item)))))

(defn discard-selection!
  [db _]
  (assoc-in db [:storage :media-selector/selected-items] []))

(defn load-selector!
  [db [_ _ {:keys [value-path] :as selector-props}]]
  ; XXX#8073
  ; A load-selector! függvény a {:value-path [...]} tulajdonságként átadott útvonalon található értéket, ...
  ; ... ha szükséges akkor vektor típusra alakítja.
  ; ... eltárolja az aktuálisan kiválasztott elemekként.
  (let [saved-selection (get-in db value-path)]
       (cond-> db (vector? saved-selection) (assoc-in [:storage :media-selector/selected-items]  saved-selection)
                  (some?   saved-selection) (assoc-in [:storage :media-selector/selected-items] [saved-selection])
                  (nil?    saved-selection) (assoc-in [:storage :media-selector/selected-items] [])
                  :store-selector-props!    (assoc-in [:storage :media-selector/selector-props] selector-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-db :storage.media-selector/discard-selection! discard-selection!)
