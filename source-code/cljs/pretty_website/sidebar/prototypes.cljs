
(ns pretty-website.sidebar.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-button-props-prototype
  ; @ignore
  ;
  ; @param (map) sidebar-props
  ;
  ; @return (map)
  [sidebar-id _]
  {:class     :pw-sidebar--menu-button
   :icon      :menu
   :icon-size :xxl
   :on-click  {:fx [:pretty-website.sidebar/show-sidebar! sidebar-id]}})

(defn sidebar-props-prototype
  ; @ignore
  ;
  ; @param (map) sidebar-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color position] :as sidebar-props}]
  ; Sets the default border position on the other side than the sidebar position,
  ; if the sidebar placed on the left, the default border position will be on
  ; the right and vica versa.
  (merge {:cover-color :black
          :fill-color  :white
          :position    :left
          :content-value-f return
          :placeholder-value-f return}
         (if border-color {:border-position (case position :right :left :right)
                           :border-width :xxs})
         (-> sidebar-props)))
