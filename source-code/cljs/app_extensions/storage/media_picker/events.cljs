
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
  [db [_ _ {:keys [filename]}]]
  (update-in db [:storage :media-picker/data-items] vector/remove-item filename))

(defn select-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:keys [filename]}]]
  (if-let [multiple? (get-in db [:storage :media-picker/meta-items :multiple?])]
          (update-in db [:storage :media-picker/data-items] vector/conj-item-once filename)
          (assoc-in  db [:storage :media-picker/data-items] [filename])))

(defn toggle-file-selection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex file-item]]
  (if (r subs/file-selected? db item-dex file-item)
      (r unselect-file!      db item-dex file-item)
      (r select-file!        db item-dex file-item)))

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

(a/reg-event-db :storage.media-picker/discard-selection! discard-selection!)

(defn load-picker!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ picker-id {:keys [value-path] :as picker-props}]]
  (let [saved-selection (get-in db value-path)]
       (-> db (assoc-in [:storage :media-picker/data-items] saved-selection)
              (assoc-in [:storage :media-picker/meta-items] picker-props)
              (assoc-in [:storage :item-lister/meta-items :new-item-options] [:create-directory! :upload-files!]))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/save-selected-items!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db (r save-selected-items! db)
       :dispatch [:ui/close-popup! :storage.media-picker/view]}))

(a/reg-event-fx
  :storage.media-picker/load-picker!
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:value-path (item-path vector)}
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! {...}]
  ;
  ; @usage
  ;  [:storage.media-picker/load-picker! :my-picker {...}]
  [a/event-vector<-id]
  ; - A picker-id azonosító nincs felhasználva sehol, kizárólag az *-id & *-props formula
  ;   egységes használata miatt adható meg.
  ; - XXX#7157
  ;   A media-picker egy popup elemen megjelenített átalakított media-browser, aminek az indítása
  ;   az [:item-browser/load-browser! ...] eseménnyel történik, ami elindítja a media-browser eszközt,
  ;   ami felismeri, hogy nem egy útvonal alapján lett elindítva és ezért a media-picker eszközt
  ;   rendereli ki.
  (fn [{:keys [db]} [_ picker-id picker-props]]
      {:db (r load-picker! db picker-id picker-props)
       :dispatch [:item-browser/load-browser! :storage :media]}))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :storage.media-picker/->file-clicked
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ item-dex item]]
      (let [db (r toggle-file-selection! db item-dex item)]
           {:db db :dispatch-if [(r subs/save-selected-items? db item-dex item)
                                 [:storage.media-picker/save-selected-items!]]})))
