
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.home-screen.views
    (:require [extensions.home-screen.config :as config]
              [layouts.surface-a.api         :as surface-a]
              [x.app-elements.api            :as elements]

              ; TEMP
              [extensions.storage.api :as storage]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  []
  [:<> [elements/button {:hover-color      :highlight
                         :horizontal-align :left
                         :icon             :people
                         :indent           {:top :xxl}
                         :label            :clients
                         :on-click         [:router/go-to! "/@app-home/clients"]
                         :style {:width "240px"}}]
       [elements/button {:hover-color      :highlight
                         :horizontal-align :left
                         :icon             :folder
                         :indent           {:bottom :xxl :top :xxl}
                         :label            :storage
                         :on-click         [:router/go-to! "/@app-home/storage"]
                         :style {:width "240px"}}]
       [storage/media-picker {:label      "Borítóképek"
                              :value-path [:xxx]}]])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'view-structure}])
