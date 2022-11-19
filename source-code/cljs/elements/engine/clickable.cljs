
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.engine.clickable
    (:require [candy.api                     :refer [param]]
              [dom.api                       :as dom]
              [elements.engine.element       :as element]
              [elements.element.side-effects :as element.side-effects]
              [hiccup.api                    :as hiccup]
              [map.api                       :as map]
              [re-frame.api                  :as r :refer [r]]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clickable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :href (string)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :on-right-click (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :href (string)
  ;   :id (string)
  ;   :on-click (function)
  ;   :on-context-menu (function)
  ;   :on-mouse-up (function)}
  [element-id {:keys [disabled? href on-click on-right-click stop-propagation?]}]
  (cond-> {; XXX#4460 (source-code/cljs/elements/button/helpers.cljs)
           :id (hiccup/value element-id "body")}
          (boolean disabled?) (merge {:disabled true
                                      :on-click #(if stop-propagation? (dom/stop-propagation! %))})
          (not     disabled?) (merge {:href         (param href)
                                      ; A stated & clickable elemek on-click eseménye elérhető a Re-Frame
                                      ; adatbázisból is, ezért esemény alapon is meghívhatók, így lehetséges
                                      ; a keypress-handler által is vezérelhetővé tenni a clickable
                                      ; elemeket.
                                      ; A static & clickable elemek on-click esemény kizárólag függvényként
                                      ; hívható meg.
                                      :on-click #(do (if stop-propagation? (dom/stop-propagation! %))
                                                     (r/dispatch on-click))
                                      :on-mouse-up #(element.side-effects/blur-element! element-id)}
                                     (if on-right-click {:on-context-menu #(do (.preventDefault %)
                                                                               (r/dispatch on-right-click)
                                                                               (element.side-effects/blur-element! element-id))}))))
