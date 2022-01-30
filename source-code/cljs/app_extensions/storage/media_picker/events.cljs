
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
  (-> db (assoc-in [:storage :media-picker/meta-items] picker-props)
         (assoc-in [:storage :item-lister/meta-items :new-item-options] [:create-directory! :upload-files!])))



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
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:value-path (item-path vector)}
  [a/event-vector<-id]
  ; - A picker-id azonosító nincs felhasználva sehol, kizárólag az *-id & *-props formula
  ;   egyésges használata miatt adható meg.
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
      {:db (r ->file-clicked db item-dex item)}))
