
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.4.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.horizontal-line
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;  {:fade (keyword)(opt)
  ;   :strength (px)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:height (string)}}
  [_ {:keys [fade strength] :as line-props}]
  (cond-> {:style {:height (css/px strength)}}
          (some? fade) (assoc :data-fade fade)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :layout (keyword)
  ;   :strength (px)}
  [line-props]
  (merge {:color    :muted
          :layout   :fit
          :strength 1}
         (param line-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;
  ; @return (hiccup)
  [line-id line-props]
  [:div.x-horizontal-line (engine/element-attributes line-id line-props)
                          [:div.x-horizontal-line--body (line-body-attributes line-id line-props)]])

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ;  {:color (keyword)(opt)
  ;    :highlight, :muted, :primary, :secondary
  ;    Default: :muted
  ;   :fade (keyword)(opt)
  ;    :in, :out, :in-out
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;   :strength (px)(opt)
  ;    Default: 1}
  ;
  ; @usage
  ;  [elements/horizontal-line {...}]
  ;
  ; @usage
  ;  [elements/horizontal-line :my-horizontal-line {...}]
  ;
  ; @return (component)
  ([line-props]
   [element (a/id) line-props])

  ([line-id line-props]
   (let [line-props (a/prot line-props line-props-prototype)]
        [horizontal-line line-id line-props])))
