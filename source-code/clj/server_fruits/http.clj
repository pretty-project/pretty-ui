
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-fruits.http
    (:require [mid-fruits.candy :refer [param return]]))



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
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:path-params param-key]))

(defn request->params
  ; @param (map) request
  ;  {:params (map)}
  ;
  ; @return (map)
  [{:keys [params]}]
  (return params))

(defn request->param
  ; @param (map) request
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:params param-key]))

(defn request->transit-params
  ; @param (map) request
  ;  {:transit-params (map)}
  ;
  ; @return (map)
  [{:keys [transit-params]}]
  (return transit-params))

(defn request->transit-param
  ; @param (map) request
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:transit-params param-key]))

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
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:transit-params :source param-key]))

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
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:multipart-params param-key]))

(defn request->session
  ; @param (map) request
  ;  {:session (map)}
  ;
  ; @return (map)
  [{:keys [session]}]
  (return session))

(defn request->session-param
  ; @param (map) request
  ; @param (keyword) param-key
  ;
  ; @return (*)
  [request param-key]
  (get-in request [:session param-key]))

(defn request->route-path
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



;; -- Default wrapper ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn response-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :headers (map)(opt)
  ;   :mime-type (string)(opt)
  ;    Default: "text/plain"
  ;   :session (map)(opt)
  ;   :status (integer)(opt)
  ;    Default: 200}
  ;
  ; @example
  ;  (http/response-wrap {:body "foo"})
  ;  =>
  ;  {:body    "foo"
  ;   :headers {"Content-Type" "text/plain"}
  ;   :status  200}
  ;
  ; @example
  ;  (http/response-wrap {:body      "foo"
  ;                       :headers   {"Content-Disposition" "inline"}
  ;                       :mime-type "text/plain"})
  ;  =>
  ;  {:body    "foo"
  ;   :headers {"Content-Type"        "text/plain"
  ;             "Content-Disposition" "inline"}
  ;   :status  200}
  ;
  ; @return (map)
  ;  {:body (string)
  ;   :headers (map)
  ;   :session (map)
  ;   :status (integer)}
  [{:keys [headers mime-type] :as response-props}]
  ; WARNING!
  ; A :body és :mime-type tulajdonságok megadása szükséges a response-wrap függvény használatához!
  (let [headers (merge {"Content-Type" (or mime-type "text/plain")} headers)]
       (merge {:headers headers :status 200}
              (select-keys response-props [:body :session :status]))))



;; -- Specific wrappers -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-wrap
  ; @param (map) response-props
  ;  {:error-message (string or keyword)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/error-wrap {:error-message "File not found"
  ;                    :status        404}
  ;  =>
  ;  {:body    ":file-not-found"
  ;   :headers {"Content-Type" "text/plain"}
  ;   :status  404}
  ;
  ; @return (map)
  [{:keys [error-message] :as response-props}]
  (response-wrap (merge {:body   (str   error-message)
                         :status (param 500)}
                        (select-keys response-props [:session :status]))))

(defn html-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/html-wrap {:body "<!DOCTYPE html> ..."})
  ;  =>
  ;  {:body    "<!DOCTYPE html> ..."
  ;   :headers {"Content-Type" "text/html"}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:body      (str body)
                         :mime-type "text/html"}
                        (select-keys response-props [:session :status]))))

(defn json-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}

  ;
  ; @example
  ;  (http/json-wrap {:body "{...}"})
  ;  =>
  ;  {:body    "{...}"
  ;   :headers {"Content-Type" "application/json"}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:body      (str body)
                         :mime-type "application/json"}
                        (select-keys response-props [:session :status]))))

(defn map-wrap
  ; @param (map) response-props
  ;  {:body (map)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/map-wrap {:body {...})
  ;  =>
  ;  {:body    "{...}"
  ;   :headers {"Content-Type" "text/plain"}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:body (str body)}
                        (select-keys response-props [:session :status]))))

(defn media-wrap
  ; @param (map) response-props
  ;  {:body (java.io.File object)
  ;   :filename (string)(opt)
  ;   :mime-type (string)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/media-wrap {:body      #object[java.io.File 0x4571e67a "/my-file.png"
  ;                    :mime-type "image/png"})
  ;  =>
  ;  {:body    #object[java.io.File 0x4571e67a "/my-file.png"
  ;   :headers {"Content-Type" "image/png"}
  ;   :status  200}
  ;
  ; @example
  ;  (http/media-wrap {:body      #object[java.io.File 0x4571e67a "/my-file.png"
  ;                    :filename  "my-file.png"
  ;                    :mime-type "image/png"})
  ;  =>
  ;  {:body    #object[java.io.File 0x4571e67a "/my-file.png"
  ;   :headers {"Content-Type"        "image/png"
  ;             "Content-Disposition" "inline; filename=\"my-file.png\""}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body filename] :as response-props}]
  (response-wrap (merge {:body (param body)
                       ; Az attachment beállítás használatával a böngésző akkor is felkínálja mentésre,
                       ; a szerver válaszát, ha azt különben képes lenne megjeleníteni.
                       ; :headers (if filename {"Content-Disposition" "attachment; filename=\""filename"\""})
                         :headers (if filename {"Content-Disposition" "inline; filename=\""filename"\""})}
                        (select-keys response-props [:mime-type :session :status]))))

(defn xml-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/xml-wrap {:body "foo"})
  ;  =>
  ;  {:body    "foo"
  ;   :headers {"Content-Type" "application/xml"}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:body      (str body)
                         :mime-type "application/xml"}
                        (select-keys response-props [:session :status]))))

(defn text-wrap
  ; @param (map) response-props
  ;  {:body (string)
  ;   :session (map)(opt)
  ;   :status (integer)(opt)}
  ;
  ; @example
  ;  (http/text-wrap {:body "foo"})
  ;  =>
  ;  {:body    "foo"
  ;   :headers {"Content-Type" "text/plain"}
  ;   :status  200}
  ;
  ; @return (map)
  [{:keys [body] :as response-props}]
  (response-wrap (merge {:body      (str body)
                         :mime-type "text/plain"}
                        (select-keys response-props [:session :status]))))
