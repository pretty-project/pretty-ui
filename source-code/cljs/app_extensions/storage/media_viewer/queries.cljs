
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.media-viewer.queries
    (:require [app-extensions.storage.media-viewer.subs :as media-viewer.subs]
              [x.app-core.api                           :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-directory-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ viewer-id]]
  (let [directory-id (r media-viewer.subs/get-directory-id db viewer-id)]
       [:debug `(:storage.media-viewer/get-directory-item ~{:directory-id directory-id})]))
