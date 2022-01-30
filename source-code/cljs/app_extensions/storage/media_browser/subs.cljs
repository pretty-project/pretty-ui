
(ns app-extensions.storage.media-browser.subs
    (:require [app-plugins.item-browser.subs]
              [x.app-core.api :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-plugins.item-browser.subs
(def route-handled? app-plugins.item-browser.subs/route-handled?)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-browser-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#7157
  ;
  ; @return (boolean)
  [db _]
  (r route-handled? db :storage :media))
