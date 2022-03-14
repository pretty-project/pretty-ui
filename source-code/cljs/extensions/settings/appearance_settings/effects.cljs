
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.settings.appearance-settings.effects
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :settings.appearance-settings/set-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) theme-props
  ;  {:id (keyword)
  ;   :name (metamorphic-content)}
  (fn [_ [_ theme-props]]
      (let [theme-id (get theme-props :id)]
          ;[:store-the-change-on-server-side! ...]
           [:ui/change-theme! theme-id])))
