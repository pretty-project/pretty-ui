
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-picker.events
    (:require [extensions.storage.media-picker.subs :as media-picker.subs]
              [mid-fruits.vector                    :as vector]
              [x.app-core.api                       :as a :refer [r]]
              [x.app-ui.api                         :as ui]
              [x.app-media.api                      :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unselect-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (let [file-uri (media/filename->media-storage-uri filename)]
       (update-in db [:storage :media-picker/selected-items] vector/remove-item file-uri)))

(defn select-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (let [file-uri (media/filename->media-storage-uri filename)]
       (if-let [multiple? (get-in db [:storage :media-picker/picker-props :multiple?])]
               (update-in db [:storage :media-picker/selected-items] vector/conj-item-once file-uri)
               (assoc-in  db [:storage :media-picker/selected-items] [file-uri]))))

(defn toggle-file-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ file-item]]
  (if (r media-picker.subs/file-selected? db file-item)
      (r unselect-file!                   db file-item)
      (r select-file!                     db file-item)))

(defn save-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-items (get-in db [:storage :media-picker/selected-items])
        value-path     (get-in db [:storage :media-picker/picker-props :value-path])]
       (assoc-in db value-path selected-items)))

(defn discard-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (assoc-in db [:storage :media-picker/selected-items] []))

(defn load-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:keys [value-path] :as picker-props}]]
  (let [saved-selection (get-in db value-path)]
       (-> db (assoc-in [:storage :media-picker/selected-items] saved-selection)
              (assoc-in [:storage :media-picker/picker-props]   picker-props))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :storage.media-picker/discard-selection! discard-selection!)
