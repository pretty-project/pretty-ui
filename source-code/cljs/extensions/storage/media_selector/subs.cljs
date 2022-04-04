
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.media-selector.subs
    (:require [mid-fruits.candy     :refer [return]]
              [mid-fruits.io        :as io]
              [mid-fruits.vector    :as vector]
              [x.app-core.api       :as a :refer [r]]
              [x.app-media.api      :as media]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (strings in vector)
  [db _]
  (get-in db [:storage :media-selector/selected-items]))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (let [selected-items (r get-selected-items db)]
       (-> selected-items vector/nonempty? not)))

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (integer)
  [db _]
  (let [selected-items (r get-selected-items db)]
       (count selected-items)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [filename]}]]
  (let [file-uri       (media/filename->media-storage-uri filename)
        selected-items (r get-selected-items db)]
       (vector/contains-item? selected-items file-uri)))

(defn file-selectable?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [mime-type]}]]
  (if-let [extensions (get-in db [:storage :media-selector/selector-props :extensions])]
          (let [extension (io/mime-type->extension mime-type)]
               (vector/contains-item? extensions extension))
          (return true)))

(defn save-selected-items?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ file-item]]
  (let [multiple? (get-in db [:storage :media-selector/selector-props :multiple?])]
       (and (not multiple?)
            (r file-selected? db file-item))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-selector/no-items-selected? no-items-selected?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-selector/get-selected-item-count get-selected-item-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-selector/file-selected? file-selected?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :storage.media-selector/file-selectable? file-selectable?)
