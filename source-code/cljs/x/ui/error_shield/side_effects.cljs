
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.error-shield.side-effects
    (:require [dom.api                   :as dom]
              [re-frame.api              :as r]
              [time.api                  :as time]
              [x.environment.api         :as x.environment]
              [x.ui.error-shield.helpers :as error-shield.helpers]
              [x.ui.renderer             :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def send-error-report-f (str "function sendErrorReport () { var reportButton = document.getElementById(\"x-error-shield--report-button\");"
                              "                              reportButton.removeAttribute (\"onClick\");"
                              "                              reportButton.setAttribute (\"data-disabled\", \"true\");"
                              "                              reportButton.innerHTML = \"Error report sent\";"
                              "}"))

(defn set-error-shield!
  ; @param (*) content
  ;
  ; @usage
  ;  (set-error-shield! "My content")
  [content]
  (let [app-db @r/app-db]
       (dom/append-script! send-error-report-f)
       (dom/insert-as-last-of-type! (dom/get-element-by-id "x-body-container")
                                    (-> (dom/create-element! "div")
                                        (dom/set-element-id! "x-error-shield")
                                        (dom/set-element-attribute! "data-nosnippet" "true")
                                        (dom/append-element! (-> (dom/create-element! "div")
                                                                 (dom/set-element-id! "x-error-shield--body")
                                                                 (dom/append-element! (-> (dom/create-element!         "div")
                                                                                          (dom/set-element-id!         "x-error-shield--content")
                                                                                          (dom/set-element-content!    content)))
                                                                 (dom/append-element! (-> (dom/create-element!         "a")
                                                                                          (dom/set-element-id!         "x-error-shield--refresh-button")
                                                                                          (dom/set-element-attributes! {:href            "#"
                                                                                                                        :data-selectable "false"
                                                                                                                        :data-clickable  "true"
                                                                                                                        :onClick         "location.reload ()"})
                                                                                          (dom/set-element-content!    "Refresh")))
                                                                 (dom/append-element! (-> (dom/create-element!         "div")
                                                                                          (dom/set-element-id!         "x-error-shield--report-button")
                                                                                          (dom/set-element-attributes! {:onClick (str "sendErrorReport (\"""\")")
                                                                                                                        :data-selectable "false"
                                                                                                                        :data-clickable  "true"})
                                                                                          (dom/set-element-content!    "Send error report")))))))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.ui/set-error-shield! "My content"]
(r/reg-fx :x.ui/set-error-shield! set-error-shield!)
