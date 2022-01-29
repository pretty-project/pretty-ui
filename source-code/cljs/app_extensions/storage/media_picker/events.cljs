
(ns app-extensions.storage.media-picker.events
    (:require [app-plugins.item-lister.subs]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-ui.api      :as ui]
              [app-extensions.storage.engine            :as engine]
              [app-extensions.storage.media-picker.subs :as subs]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unselect-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex {:keys [filename] :as item}]]
  (update-in db [:storage :media-picker/data-items] vector/remove-item filename))

(defn select-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex {:keys [filename] :as item}]]
  (if-let [multiple? (get-in db [:storage :media-picker/meta-items :multiple?])]
          (update-in db [:storage :media-picker/data-items] vector/conj-item-once filename)
          (assoc-in  db [:storage :media-picker/data-items] [filename])))

(defn ->file-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex item]]
  (if (r subs/file-selected? db item-dex item)
      (r unselect-file!      db item-dex item)
      (r select-file!        db item-dex item)))

(defn select-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id]]
  (let [value-path (get-in db [:storage :media-picker/meta-items :value-path])]
       (letfn [(f [db item-dex]
                  (let [{:keys [id mime-type filename]} (get-in db [:storage :item-lister/data-items item-dex])]
                       (case mime-type "storage/directory" (update-in db value-path vector/conj-item id)
                                                           (update-in db value-path vector/conj-item filename))))]
              (reduce f db (r app-plugins.item-lister.subs/get-selected-item-dexes db :storage :media)))))

(defn load-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id picker-props]]
  (assoc-in db [:storage :media-picker/meta-items] picker-props))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/select-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r select-items! db)
       :dispatch [:ui/close-popup! :storage.media-picker/view]}))

(a/reg-event-fx
  :storage.media-picker/load-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) (picker-props)
  ;  {:value-path (item-path vector)}
  (fn [{:keys [db]} event-vector]
      (let [picker-id    (a/event-vector->second-id   event-vector)
            picker-props (a/event-vector->first-props event-vector)]
           {:db (r load-picker! db picker-id picker-props)
            :dispatch [:item-browser/load-browser! :storage :media]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/->file-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [a/debug!]
  (fn [{:keys [db]} [_ item-dex item]]
      {:db (r ->file-clicked db item-dex item)}))
