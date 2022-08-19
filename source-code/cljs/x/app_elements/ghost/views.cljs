

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.ghost.views
    (:require [x.app-core.api                  :as a]
              [x.app-elements.ghost.helpers    :as ghost.helpers]
              [x.app-elements.ghost.prototypes :as ghost.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ghost
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) ghost-id
  ; @param (map) ghost-props
  [ghost-id ghost-props]
  [:div.x-ghost (ghost.helpers/ghost-attributes ghost-id ghost-props)
                [:div.x-ghost--body]])

(defn element
  ; @param (keyword)(opt) ghost-id
  ; @param (map) ghost-props
  ;  {:border-radius (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :class (keyword or keywords in vector)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :s
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/ghost {...}]
  ;
  ; @usage
  ;  [elements/ghost :my-ghost {...}]
  ([ghost-props]
   [element (a/id) ghost-props])

  ([ghost-id ghost-props]
   (let [ghost-props (ghost.prototypes/ghost-props-prototype ghost-props)]
        [ghost ghost-id ghost-props])))
