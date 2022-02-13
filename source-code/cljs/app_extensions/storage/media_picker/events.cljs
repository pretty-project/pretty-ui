
(ns app-extensions.storage.media-picker.events
    (:require [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-ui.api      :as ui]
              [app-extensions.storage.engine            :as engine]
              [app-extensions.storage.media-picker.subs :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unselect-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (update-in db [:storage :media-picker/data-items] vector/remove-item filename))

(defn select-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (if-let [multiple? (get-in db [:storage :media-picker/meta-items :multiple?])]
          (update-in db [:storage :media-picker/data-items] vector/conj-item-once filename)
          (assoc-in  db [:storage :media-picker/data-items] [filename])))

(defn toggle-file-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ file-item]]
  (if (r subs/file-selected? db file-item)
      (r unselect-file!      db file-item)
      (r select-file!        db file-item)))

(defn save-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]]
  (let [selected-items (get-in db [:storage :media-picker/data-items])
        value-path     (get-in db [:storage :media-picker/meta-items :value-path])]
       (assoc-in db value-path selected-items)))

(defn discard-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]]
  (assoc-in db [:storage :media-picker/data-items] []))

(defn load-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id {:keys [value-path] :as picker-props}]]
  (let [saved-selection (get-in db value-path)]
       (-> db (assoc-in [:storage :media-picker/data-items] saved-selection)
              (assoc-in [:storage :media-picker/meta-items] picker-props)
              (assoc-in [:storage :item-lister/meta-items :new-item-options] [:create-directory! :upload-files!]))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.media-picker/discard-selection! discard-selection!)
