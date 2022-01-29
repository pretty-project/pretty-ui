
(ns app-extensions.storage.media-picker.subs
    (:require [app-plugins.item-browser.subs]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (get-in db [:storage :media-picker/data-items]))

(defn no-items-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-items (get-in db [:storage :media-picker/data-items])]
       (-> selected-items vector/nonempty? not)))

(a/reg-sub :storage.media-picker/no-items-selected? no-items-selected?)

(defn get-selected-item-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [selected-items (get-in db [:storage :media-picker/data-items])]
       (count selected-items)))

(defn file-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex {:keys [filename] :as item}]]
  (let [selected-items (r get-selected-items db)]
       (vector/contains-item? selected-items filename)))

(defn get-media-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ item-dex item]]
  {:selected? (r file-selected? db item-dex item)})

(a/reg-sub :storage.media-picker/get-media-item-props get-media-item-props)

(defn get-selection-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  {:no-items-selected?  (r no-items-selected?      db)
   :selected-item-count (r get-selected-item-count db)})

(a/reg-sub :storage.media-picker/get-selection-props get-selection-props)
