
(ns app-extensions.storage.media-browser.queries
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-db.api   :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ {:keys [id] :as media-item}]]
  [:debug `(storage.media-browser/duplicate-item! ~{})])

(defn get-update-item-alias-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ media-item item-alias]]
  (let [media-item (-> media-item (db/document->namespaced-document :media)
                                  (assoc :media/alias item-alias))]
       [:debug `(storage.media-browser/update-item! ~media-item)]))
