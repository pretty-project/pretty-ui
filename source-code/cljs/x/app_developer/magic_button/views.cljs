
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.magic-button.views
    (:require [re-frame.api       :as r]
              [x.app-elements.api :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  []
  (if-let [debug-mode-detected? @(r/subscribe [:core/debug-mode-detected?])]
          (let [db-write-count @(r/subscribe [:developer/get-db-write-count])]
               [:div {:style {:position :fixed :bottom 0 :right 0 :z-index 9999
                              :background-color "white" :border-radius "45px 0 0 0"
                              :width "60px" :height "60px"
                              :display "flex" :justify-content "flex-end"
                              :align-items "flex-end"
                              :box-shadow "0 0 5px 1px rgba(0, 0, 0, .15)"}}

                     [elements/icon-button ::element
                                           {:border-radius :xl
                                            :hover-color   :highlight
                                            :icon          :auto_fix_high
                                            :preset        :default
                                            :on-click      [:developer/render-developer-tools!]}]])))
                                           ;:keypress      {:key-code 77}
                                           ;:badge-content db-write-count
