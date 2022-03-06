
; WARNING! UNDER DEVELOPMENT! DO NOT USE!

;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-tools.pdf.engine
    (:require [clj-pdf.core       :as pdf]
              [mid-fruits.random  :as random]
              [server-fruits.http :as http]
              [server-fruits.io   :as io]
              [x.server-media.api :as media]))








;
(def content
  [{}
   [:list {:roman true}
     [:chunk {:style :bold} "a bold item"]
     "xyxy item"
     "yet another item"]
;   [:phrase "Dzsékó text"]
;   [:phrase "some more text"]
;   [:paragraph "yet more text"]

   [:graphics {:under true :translate [100 100]}
    (fn [g2d]
      (doto g2d
        (.setColor java.awt.Color/RED)
        (.drawOval (int 0) (int 0) (int 50) (int 50))
        ; Requires :register-system-fonts? true & font availability
        (.setFont (java.awt.Font. "GillSans-SemiBold" java.awt.Font/PLAIN 12))
        (.drawString "A red circle." (float -5) (float 64))))]])







;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- request->filename
  ; @param (map) request
  ;
  ; @return (string)
  [request]
  (http/request->transit-param request :filename))

(defn- request->content
  ; @param (map) request
  ;
  ; @return (vector)
  [request]
  (http/request->transit-param request :content)

  ; TEMP
  content)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn generate-pdf!
  ; @param (map) request
  ;   {:transit-params (map)
  ;    {:content (vector) XXX#9345
  ;     :filename (string)}}
  ;
  ; @return (map)
  [request]
  (let [content  (request->content  request)
        filename (request->filename request)
        temporary-filename (str (random/generate-string) ".pdf")
        temporary-filepath (media/filename->temporary-filepath temporary-filename)]
       (pdf/pdf content temporary-filepath)
       (http/map-wrap {:body {:temporary-filename temporary-filename
                              :filename filename}})))

(defn download-generated-pdf
  ; @param (map) request
  ;
  ; @return (map)
  [request])
  ;(media/download-temporary-file request))
