
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.home-screen.views
    (:require [extensions.home-screen.config :as config]
              [x.app-layouts.api             :as layouts]

              ; TEMP
              [extensions.storage.api :as storage]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div [layouts/layout-b surface-id {:cards config/CARDS}]
        [storage/media-picker {:label "Borítóképek"
                               :value-path [:xxx]}]])
