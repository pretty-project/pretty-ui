
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.sidebar-a.views
    (:require [layouts.surface-a.helpers :as helpers]
              [react.api                 :as react]
              [reagent.api               :as reagent]
              [re-frame.api              :as r]
              [x.app-components.api      :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)}
  [sidebar-id {:keys [content] :as layout-props}]
  [:div#sidebar-a (helpers/layout-attributes sidebar-id layout-props)
                  [:div#sidebar-a--content {:data-hide-scrollbar true}
                                           [x.components/content sidebar-id content]]])

(defn layout
  ; @param (keyword) sidebar-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [layout :my-sidebar {...}]
  [sidebar-id {:keys [on-mount on-unmount] :as layout-props}]
  (let [] ;layout-props (prototypes/layout-props-prototype layout-props)
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                            :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                            :reagent-render         (fn [_ _] [sidebar-a sidebar-id layout-props])})))
