
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
          (let [hide-db-write-count? @(r/subscribe [:x.db/get-item [:developer-tools :core/meta-items :hide-db-write-count?]])
                db-write-count       @(r/subscribe [:developer-tools.core/get-db-write-count])]
               [:div {:style {:position :fixed :bottom 0 :right 0 :z-index 9999
                              :background-color "white" :border-radius "45px 0 0 0"
                              :width "60px" :height "60px" :align-items "flex-end"
                              :display "flex" :justify-content "flex-end"
                              :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"}}
                     [:div {:style {:display (if hide-db-write-count? "none" "flex")
                                    :position "absolute"
                                    :bottom "0"
                                    :left "-80px"
                                    :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"
                                    :background "white"
                                    :border-radius "15px 15px 0 0"
                                    :height "25px"
                                    :min-width "50px"
                                    :padding "0 12px"
                                    :justify-content "center"
                                    :align-items "center"
                                    :line-height "25px"
                                    :font-weight "600"
                                    :font-size "12px"
                                    :overflow "hidden"}
                                    ;:animation-name (if (even? db-write-count) "debug" "none")}
                            :data-debug (even? db-write-count)}
                           (str db-write-count)]
                     [elements/icon-button ::element
                                           {:border-radius :xl
                                            :hover-color   :highlight
                                            :icon          :auto_fix_high
                                            :preset        :default
                                            :on-click      [:developer-tools.magic-widget/render-widget!]}]])))
                                           ;:keypress      {:key-code 77}
                                            ;:badge-content (if show-db-write-count? db-write-count)}]])))
