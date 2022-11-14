
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.magic-button.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  []
  (if-let [debug-mode-detected? @(r/subscribe [:x.core/debug-mode-detected?])]
          (let [show-db-write-count? @(r/subscribe [:x.db/get-item [:developer-tools :core/meta-items :show-db-write-count?]])
                db-write-count       @(r/subscribe [:developer-tools.core/get-db-write-count])]
               [:div {:style {:position :fixed :bottom 0 :right 0 :z-index 9999
                              :background-color "white" :border-radius "45px 0 0 0"
                              :width "60px" :height "60px" :align-items "flex-end"
                              :display "flex" :justify-content "flex-end"
                              :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"}}
                     [elements/icon-button ::element
                                           {:border-radius :xl
                                            :hover-color   :highlight
                                            :icon          :auto_fix_high
                                            :preset        :default
                                            :on-click      [:developer-tools.magic-widget/render-widget!]
                                           ;:keypress      {:key-code 77}
                                            :badge-content (if show-db-write-count? db-write-count)}]])))
