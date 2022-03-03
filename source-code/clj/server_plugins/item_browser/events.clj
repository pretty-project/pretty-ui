
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.events
    (:require [mid-plugins.item-browser.events    :as events]
              [server-plugins.item-browser.engine :as engine]
              [x.server-core.api                  :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.events
(def store-browser-props! events/store-browser-props!)
(def store-lister-props!  events/store-lister-props!)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-browser!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [lister-props  (select-keys browser-props engine/LISTER-PROPS-KEYS)
        browser-props (select-keys browser-props engine/BROWSER-PROPS-KEYS)]
       (as-> db % (r store-lister-props!  % extension-id item-namespace lister-props)
                  (r store-browser-props! % extension-id item-namespace browser-props))))
