
(ns app-extensions.storage.media-viewer.queries
    (:require [x.app-core.api :as a :refer [r]]
              [app-extensions.storage.media-viewer.subs :as subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-directory-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  (let [directory-id (r subs/get-directory-id db viewer-id)]
       [:debug `(:storage.media-viewer/get-directory-item ~{:directory-id directory-id})]))
