
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-picker.subs
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.io        :as io]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-media.api      :as media]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-saved-selection
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [value-path (get-in db [:storage :media-picker/meta-items :value-path])]
       (get-in db value-path)))

(defn get-selected-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:storage :media-picker/data-items]))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-items (get-in db [:storage :media-picker/data-items])]
       (-> selected-items vector/nonempty? not)))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-items (get-in db [:storage :media-picker/data-items])]
       (count selected-items)))

(defn file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (let [file-uri       (media/filename->media-storage-uri filename)
        selected-items (r get-selected-items db)]
       (vector/contains-item? selected-items file-uri)))

(defn file-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [mime-type]}]]
  (if-let [extensions (get-in db [:storage :media-picker/meta-items :extensions])]
          (let [extension (io/mime-type->extension mime-type)]
               (vector/contains-item? extensions extension))
          (return true)))

(defn save-selected-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ file-item]]
  (let [multiple? (get-in db [:storage :media-picker/meta-items :multiple?])]
       (and (not multiple?)
            (r file-selected? db file-item))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/no-items-selected? no-items-selected?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/get-selected-item-count get-selected-item-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/file-selected? file-selected?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-picker/file-selectable? file-selectable?)
