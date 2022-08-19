

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.horizontal-line
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
  ;  {:strength (px)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:height (string)}}
  [_ {:keys [strength] :as line-props}]
  (cond-> {:style {:height (css/px strength)}}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ;  {:color (keyword or string)
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
  [line-id line-props]
  [:div.x-horizontal-line (engine/element-attributes line-id line-props)
                          [:div.x-horizontal-line--body (line-body-attributes line-id line-props)]])

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ;  {:color (keyword or string)(opt)
  ;    :highlight, :muted, :primary, :secondary
  ;    Default: :muted
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
  ([line-props]
   [element (a/id) line-props])

  ([line-id line-props]
   (let [line-props (line-props-prototype line-props)]
        [horizontal-line line-id line-props])))
