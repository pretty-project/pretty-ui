
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.home-screen.views
    (:require [x.app-layouts.api                 :as layouts]
              [app-extensions.home-screen.config :as config]

              ; TEMP
              [app-extensions.storage.api :as storage]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div [layouts/layout-b surface-id {:cards config/CARDS}]
        [storage/media-picker {:label "Borítóképek"
                               :value-path [:xxx]}]])
