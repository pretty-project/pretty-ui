
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.11
; Description:
; Version: v0.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.http
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.reader :as reader]
              [mid-fruits.string :as string]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->cookies
  ; @param (map) request
  ;  {:cookies (map)}
  ;
  ; @return (map)
  [{:keys [cookies]}]
  (return cookies))

(defn request->cookie
  ; @param (map) request
  ;  {:cookies (map)}
  ; @param (string) cookie-id
  ;
  ; @return (map)
  [request cookie-id]
  (get-in request [:cookies cookie-id]))

(defn request->query-string
  ; @param (map) request
  ;  {:query-string (string)}
  ;
  ; @return (string)
  [{:keys [query-string]}]
  (return query-string))

(defn request->form-params
  ; @param (map) request
  ;  {:form-params (map)}
  ;
  ; @return (map)
  [{:keys [form-params]}]
  (return form-params))

(defn request->form-param
  ; @param (map) request
  ;  {:form-params (map)}
  ; @param (string) element-name
  ;
  ; @return (*)
  [request element-name]
  (-> request :form-params (get element-name)))

(defn request->path-params
  ; @param (map) request
  ;  {:path-params (map)}
  ;
  ; @return (map)
  [{:keys [path-params]}]
  (return path-params))

(defn request->path-param
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:path-params param-id]))

(defn request->params
  ; @param (map) request
  ;  {:params (map)}
  ;
  ; @return (map)
  [{:keys [params]}]
  (return params))

(defn request->param
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:params param-id]))

(defn request->transit-params
  ; @param (map) request
  ;  {:transit-params (map)}
  ;
  ; @return (map)
  [{:keys [transit-params]}]
  (return transit-params))

(defn request->transit-param
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:transit-params param-id]))

(defn request->source-params
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:source (map)}}
  ;
  ; @return (map)
  [{:keys [transit-params]}]
  (get transit-params :source))

(defn request->source-param
  ; @param (map) request
  ;  {:transit-params (map)
  ;   {:source (map)}}
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:transit-params :source param-id]))

(defn request->multipart-params
  ; @param (map) request
  ;  {:multipart-params (map)}
  ;
  ; @return (map)
  [{:keys [multipart-params]}]
  (return multipart-params))

(defn request->multipart-param
  ; @param (map) request
  ;  {:multipart-params (map)}
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:multipart-params param-id]))

(defn request->session
  ; @param (map) request
  ;  {:session (map)}
  ;
  ; @return (map)
  [{:keys [session]}]
  (return session))

(defn request->session-param
  ; @param (map) request
  ; @param (keyword) param-id
  ;
  ; @return (*)
  [request param-id]
  (get-in request [:session param-id]))

(defn request->route-template
  ; @param (map) request
  ;  {:uri (string)}
  ;
  ; @return (string)
  [{:keys [uri]}]
  (str uri))

(defn request->uri
  ; @param (map) request
  ;  {:server-name (string)
  ;   :uri (string)}
  ;
  ; @return (string)
  [{:keys [server-name uri]}]
  (str server-name uri))

(defn request->route-path
  ; @param (map) request
  ;  {:uri (string)}
  ;
  ; @return (string)
  [{:keys [uri]}]
  (str uri))



;; -- Wrappers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn response-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :mime-type (string)
  ;   :error-message (keyword or string)
  ;   :session (map)
  ;   :status (integer)}
  ;
  ; @usage
  ;  (http/response-wrap {:body      "foo"
  ;                       :mime-type "text/plain"
  ;                       :status    200})
  ;
  ; @return (map)
  ;  {:body (string)
  ;   :headers (map)
  ;   :status (integer)}
  [{:keys [mime-type] :as response-props}]
  (merge {:headers {"Content-Type" mime-type}}
         (dissoc response-props :mime-type)))

(defn error-wrap
  ; @param (map) response-props
  ;  {:error-message (string or keyword)
  ;   :status (integer)}
  ;
  ; @usage
  ;  (http/error-wrap {:error-message :file-not-found
  ;                    :status        404}
  ;
  ; @return (map)
  [{:keys [error-message] :as response-props}]
  (response-wrap (merge {:body (str error-message)
                         :mime-type "text/plain"}
                        (param response-props))))

(defn html-wrap
  ; @param (map) response-props
  ;  {:body (string)}
  ;
  ; @usage
  ;  (http/html-wrap {:body "<!DOCTYPE html> ..."})
  ;
  ; @return (map)
  [response-props]
  (response-wrap (merge {:mime-type "text/html"
                         :status    200}
                        (param response-props))))

(defn map-wrap
  ; @param (map) response-props
  ;  {:body (map)}
  ;
  ; @usage
  ;  (http/map-wrap {:body {...})
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:mime-type "text/plain"
                         :status    200}
                        (param response-props)
                        {:body (str body)})))

(defn media-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :mime-type (string)}
  ;
  ; @usage
  ;  (http/media-wrap {:body "..."
  ;                    :mime-type "image/png"})
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:status 200}
                        (param response-props))))

(defn text-wrap
  ; @param (map) response-props
  ;  {:body (string)}
  ;
  ; @usage
  ;  (http/text-wrap {:body "foo"})
  ;
  ; @return (map)
  [response-props]
  (response-wrap (merge {:mime-type "text/plain"
                         :status    200}
                        (param response-props))))
