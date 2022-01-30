
(ns app-extensions.storage.media-browser.subs
    (:require [x.app-core.api   :as a :refer [r]]
              ; TEMP
              [app-plugins.item-browser.subs]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-browser-mode?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; XXX#7157
  ;
  ; @return (boolean)
  [db _]
  (r app-plugins.item-browser.subs/route-handled? db :storage :media))
