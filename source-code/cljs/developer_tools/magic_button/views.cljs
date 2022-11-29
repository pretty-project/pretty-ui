
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
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

(defn- db-write-indicator
  []
  (let [db-write-count @(r/subscribe [:developer-tools.core/get-db-write-count])]
       [:div {:style {:display "flex"
                      :position "fixed"
                      :bottom "0"
                      :right "100px"
                      :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"
                      :background "white"
                      :border-radius "15px 15px 0 0"
                      :height "24px"
                      :padding "0 24px 0 12px"
                      :justify-content "center"
                      :align-items "center"
                      :line-height "24px"
                      :font-weight "600"
                      :font-size "11px"
                      :overflow "hidden"
                      :min-width "324px"
                      :gap "12px"}
                      ;:animation-name (if (even? db-write-count) "debug" "none")}
              :data-debug (even? db-write-count)}
             [elements/icon {:icon :waterfall_chart :size :xxs :color :muted}]
             [:span {:style {:color "var(--color-muted)"}} "Connected: Re-Frame DB"]
             [:span "Writes: " db-write-count]]))

(defn- magic-button
  []
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
                               :on-click      [:developer-tools.magic-widget/render-widget!]}]])
                              ;:keypress      {:key-code 77}
                               ;:badge-content (if show-db-write-count? db-write-count)}]])))

(defn element
  []
  (if-let [debug-mode-detected? @(r/subscribe [:x.core/debug-mode-detected?])]
          (let [hide-db-write-count? @(r/subscribe [:x.db/get-item [:developer-tools :core/meta-items :hide-db-write-count?]])]
               [:<> [magic-button]
                    (if-not hide-db-write-count? [db-write-indicator])])))
