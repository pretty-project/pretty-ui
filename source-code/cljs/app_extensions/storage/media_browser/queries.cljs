
(ns app-extensions.storage.media-browser.queries
    (:require [x.app-core.api :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-duplicate-media-item-query
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) media-item
  ;  {:id (string)}
  ;
  ; @return (vector)
  [db [_ {:keys [id]}]]
  [:debug])
