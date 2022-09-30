
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.plugin-handler.body.views
    (:require [x.app-elements.api :as elements]))



;; -- Error components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-occured-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  [_ _]
  [elements/label ::error-occured-label
                  {:color            :warning
                   :content          :an-error-occured
                   :font-size        :m
                   :horizontal-align :center
                   :indent           {:top :xxl}}])

(defn error-description-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  [_ {:keys [error-description]}]
  [elements/label ::error-description-label
                  {:color            :muted
                   :content          error-description
                   :horizontal-align :center
                   :indent           {:bottom :xxl}}])

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) plugin-id
  ; @param (map) error-props
  ;  {:error-description (metamorphic-content)}
  [plugin-id error-props]
  [:<> [error-occured-label     plugin-id error-props]
       [error-description-label plugin-id error-props]])
