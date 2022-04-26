
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.body.views
    (:require [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-occured-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  [_ _]
  [elements/label ::error-occured-label
                  {:color     :warning
                   :content   :an-error-occured
                   :font-size :m}])

(defn error-description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  [_ {:keys [error-description]}]
  [elements/label ::error-description-label
                  {:color   :muted
                   :content error-description}])

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  ;  {:error-description (metamorphic-content)}
  [plugin-id error-props]
  [:<> [error-occured-label     plugin-id error-props]
       [error-description-label plugin-id error-props]])
